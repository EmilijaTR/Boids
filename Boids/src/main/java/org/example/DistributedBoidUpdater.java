package org.example;

import mpi.MPI;
import javax.swing.*;
import java.util.ArrayList;

public class DistributedBoidUpdater implements BoidUpdater {
    private JSlider cohesionSlider, alignmentSlider, separationSlider;
    private Boids boidsPanel;
    private int numBoids;
    private int width;
    private int height;
    private double initialSpeed;
    private Timer timer;
    private boolean isRootProcess = false;

    @Override
    public void setParameters(int numBoids, int width, int height, double initialSpeed) {
        this.numBoids = numBoids;
        this.width = width;
        this.height = height;
        this.initialSpeed = initialSpeed;
    }

    @Override
    public void setSliders(JSlider cohesion, JSlider alignment, JSlider separation) {
        this.cohesionSlider = cohesion;
        this.alignmentSlider = alignment;
        this.separationSlider = separation;
    }

    @Override
    public void setBoidsPanel(Boids boidsPanel) {
        this.boidsPanel = boidsPanel;
        this.isRootProcess = MPI.COMM_WORLD.Rank() == 0;

        if (isRootProcess) {
            ArrayList<Boid> flock = new ArrayList<>();
            for (int i = 0; i < numBoids; i++) {
                flock.add(new Boid(width, height, initialSpeed));
            }
            boidsPanel.setFlock(flock);
            System.out.println("Initialized " + numBoids + " boids on root");
        }
    }

    @Override
    public void start() {
        int rank = MPI.COMM_WORLD.Rank();

        if (isRootProcess) {
            timer = new Timer(16, e -> updateBoids());
            timer.start();
        } else {
            while (true) {
                updateBoids();
            }
        }
    }

    private void updateBoids() {
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        // Broadcast weights from root
        double[] weights = new double[3];
        if (rank == 0) {
            weights[0] = cohesionSlider.getValue() / 50.0;
            weights[1] = alignmentSlider.getValue() / 50.0;
            weights[2] = separationSlider.getValue() / 50.0;
        }
        MPI.COMM_WORLD.Bcast(weights, 0, 3, MPI.DOUBLE, 0);
        double cohesionWeight = weights[0];
        double alignmentWeight = weights[1];
        double separationWeight = weights[2];

        // Prepare data structures
        double[] positions = null;
        double[] velocities = null;
        int[] boidCounts = new int[size];
        int[] displacements = new int[size];

        if (rank == 0) {
            ArrayList<Boid> flock = boidsPanel.getFlock();
            positions = new double[flock.size() * 3];
            velocities = new double[flock.size() * 3];

            for (int i = 0; i < flock.size(); i++) {
                Boid b = flock.get(i);
                positions[i*3] = b.getPosition().x;
                positions[i*3+1] = b.getPosition().y;
                positions[i*3+2] = b.getPosition().z;

                velocities[i*3] = b.getVelocity().x;
                velocities[i*3+1] = b.getVelocity().y;
                velocities[i*3+2] = b.getVelocity().z;
            }

            // Calculate distribution
            int baseChunk = flock.size() / size;
            int remainder = flock.size() % size;
            for (int i = 0; i < size; i++) {
                boidCounts[i] = (baseChunk + (i < remainder ? 1 : 0)) * 3;
                displacements[i] = (i == 0) ? 0 : displacements[i-1] + boidCounts[i-1];
            }
        }

        // Broadcast counts and displacements
        MPI.COMM_WORLD.Bcast(boidCounts, 0, size, MPI.INT, 0);
        MPI.COMM_WORLD.Bcast(displacements, 0, size, MPI.INT, 0);

        // Prepare receive buffers
        int myCount = boidCounts[rank];
        double[] myPositions = new double[myCount];
        double[] myVelocities = new double[myCount];

        // Scatter data
        MPI.COMM_WORLD.Scatterv(positions, 0, boidCounts, displacements, MPI.DOUBLE,
                myPositions, 0, myCount, MPI.DOUBLE, 0);
        MPI.COMM_WORLD.Scatterv(velocities, 0, boidCounts, displacements, MPI.DOUBLE,
                myVelocities, 0, myCount, MPI.DOUBLE, 0);

        // Broadcast full flock data for flocking rules
        double[] fullFlockPositions = null;
        double[] fullFlockVelocities = null;
        if (rank == 0) {
            fullFlockPositions = positions.clone();
            fullFlockVelocities = velocities.clone();
        } else {
            fullFlockPositions = new double[boidCounts[0] * size]; // Approximate size
            fullFlockVelocities = new double[boidCounts[0] * size];
        }
        MPI.COMM_WORLD.Bcast(fullFlockPositions, 0, fullFlockPositions.length, MPI.DOUBLE, 0);
        MPI.COMM_WORLD.Bcast(fullFlockVelocities, 0, fullFlockVelocities.length, MPI.DOUBLE, 0);

        // Create local flock for processing
        ArrayList<Boid> localFlock = new ArrayList<>();
        for (int i = 0; i < myCount / 3; i++) {
            Boid b = new Boid(width, height, initialSpeed);
            b.getPosition().x = myPositions[i*3];
            b.getPosition().y = myPositions[i*3+1];
            b.getPosition().z = myPositions[i*3+2];
            b.getVelocity().x = myVelocities[i*3];
            b.getVelocity().y = myVelocities[i*3+1];
            b.getVelocity().z = myVelocities[i*3+2];
            localFlock.add(b);
        }

        // Create full flock for rules
        ArrayList<Boid> fullFlock = new ArrayList<>();
        for (int i = 0; i < fullFlockPositions.length / 3; i++) {
            Boid b = new Boid(width, height, initialSpeed);
            b.getPosition().x = fullFlockPositions[i*3];
            b.getPosition().y = fullFlockPositions[i*3+1];
            b.getPosition().z = fullFlockPositions[i*3+2];
            b.getVelocity().x = fullFlockVelocities[i*3];
            b.getVelocity().y = fullFlockVelocities[i*3+1];
            b.getVelocity().z = fullFlockVelocities[i*3+2];
            fullFlock.add(b);
        }

        // Process each boid
        for (Boid b : localFlock) {
            b.edges(width, height);
            b.flock(fullFlock, cohesionWeight, alignmentWeight, separationWeight);
            b.update();
        }

        // Store updated values
        for (int i = 0; i < localFlock.size(); i++) {
            Boid b = localFlock.get(i);
            myPositions[i*3] = b.getPosition().x;
            myPositions[i*3+1] = b.getPosition().y;
            myPositions[i*3+2] = b.getPosition().z;
            myVelocities[i*3] = b.getVelocity().x;
            myVelocities[i*3+1] = b.getVelocity().y;
            myVelocities[i*3+2] = b.getVelocity().z;
        }

        // Gather results
        MPI.COMM_WORLD.Gatherv(myPositions, 0, myCount, MPI.DOUBLE,
                positions, 0, boidCounts, displacements, MPI.DOUBLE, 0);
        MPI.COMM_WORLD.Gatherv(myVelocities, 0, myCount, MPI.DOUBLE,
                velocities, 0, boidCounts, displacements, MPI.DOUBLE, 0);
        if (rank == 0) {
            System.out.println("Total boids received: " + (positions.length / 3));
        }

        // Root process updates display
        if (rank == 0) {
            ArrayList<Boid> flock = boidsPanel.getFlock();
            for (int i = 0; i < flock.size(); i++) {
                Boid b = flock.get(i);
                if (i < positions.length / 3) {  // Safety check
                    b.getPosition().x = positions[i*3];
                    b.getPosition().y = positions[i*3+1];
                    b.getPosition().z = positions[i*3+2];
                    b.getVelocity().x = velocities[i*3];
                    b.getVelocity().y = velocities[i*3+1];
                    b.getVelocity().z = velocities[i*3+2];
                }
            }

            System.out.printf("Boid 0 updated to: (%.1f,%.1f,%.1f)\n",
                    flock.get(0).getPosition().x,
                    flock.get(0).getPosition().y,
                    flock.get(0).getPosition().z);

            SwingUtilities.invokeLater(() -> {
                boidsPanel.repaint();
            });
        }
    }
}
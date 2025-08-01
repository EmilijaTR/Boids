package org.example;

import javax.swing.*;
import java.util.ArrayList;

public class SequentialBoidUpdater implements BoidUpdater {
    private JSlider cohesionSlider, alignmentSlider, separationSlider;
    private Boids boidsPanel;

    private int numBoids;
    private int width;
    private int height;
    private double initialSpeed;

    private Timer timer;

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
        ArrayList<Boid> flock = new ArrayList<>();
        for (int i = 0; i < numBoids; i++) {
            flock.add(new Boid(width, height, initialSpeed));
        }
        boidsPanel.setFlock(flock);
    }

    @Override
    public void start() {
        timer = new Timer(16, e -> {
            ArrayList<Boid> flock = boidsPanel.getFlock();

            double cohesionWeight = cohesionSlider.getValue() / 50.0;
            double alignmentWeight = alignmentSlider.getValue() / 50.0;
            double separationWeight = separationSlider.getValue() / 50.0;

            int currentWidth = boidsPanel.getWidth();
            int currentHeight = boidsPanel.getHeight();

            for (Boid boid : flock) {
                boid.edges(currentWidth, currentHeight);
                boid.flock(flock, cohesionWeight, alignmentWeight, separationWeight);
                boid.update();
            }
            boidsPanel.repaint();
        });
        timer.start();
    }
}

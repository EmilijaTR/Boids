package org.example;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

public class Boid implements Serializable {
    private static final long serialVersionUID = 1L;

    private Vector3D position;
    private Vector3D velocity;
    private Vector3D acceleration;

    private double maxForce = 0.3;
    private double maxSpeed;

    public Boid(double width, double height, double speed) {
//        this.position = new Vector3D(Math.random() * width, Math.random() * height, Math.random() * 100); // depth max 100
        this.position = new Vector3D(
                Math.random() * width,
                Math.random() * height,
                30 + Math.random() * 40  // Ensures visible size
        );
        this.velocity = Vector3D.random3D();
        this.velocity.multiply(2 + Math.random() * 2);
        this.acceleration = new Vector3D(0, 0, 0);
        this.maxSpeed = speed;
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void edges(double width, double height) {
        System.out.println("Checking edges for position: " + position);
        if (position.x > width) position.x = 0;
        else if (position.x < 0) position.x = width;

        if (position.y > height) position.y = 0;
        else if (position.y < 0) position.y = height;

        // For wrapping in depth (optional — or you can let z float freely)
        if (position.z > 100) position.z = 0;
        else if (position.z < 0) position.z = 100;
    }
    public Vector3D getPosition() {
        return position;
    }


    public Vector3D alignment(ArrayList<Boid> boids) {
        double perceptionRadius = 25;
        Vector3D steering = new Vector3D(0, 0, 0);
        int total = 0;

        for (Boid other : boids) {
            double d = position.distance(other.position);
            if (other != this && d < perceptionRadius) {
                steering.add(other.velocity);
                total++;
            }
        }

        if (total > 0) {
            steering.divide(total); //to avg it
            steering.normalize();   //to only consider direction, making it unit vector
            steering.multiply(maxSpeed);    //give appropriate speed(mag)
            steering.subtract(velocity);    //direction in order to align
            steering.limit(maxForce);       //limit so it doesn't change all of a sudden
        }

        return steering;
    }

    public Vector3D cohesion(ArrayList<Boid> boids) {
        double perceptionRadius = 75;
        Vector3D steering = new Vector3D(0, 0, 0);
        int total = 0;

        for (Boid other : boids) {
            double d = position.distance(other.position);
            if (other != this && d < perceptionRadius) {
                steering.add(other.position);
                total++;
            }
        }

        if (total > 0) {
            steering.divide(total);
            steering.subtract(position);
            steering.normalize();
            steering.multiply(maxSpeed);
            steering.subtract(velocity);
            steering.limit(maxForce);
        }
        return steering;
    }

    public Vector3D separation(ArrayList<Boid> boids) {
        double perceptionRadius = 50;
        Vector3D steering = new Vector3D(0, 0, 0);
        int total = 0;

        for (Boid other : boids) {
            double d = position.distance(other.position);
            if (other != this && d < perceptionRadius) {
                Vector3D diff = Vector3D.subtract(position, other.position);    //direction in oposite
                diff.divide(d * d);   //closer the boid, stronger force to move
                steering.add(diff);
                total++;
            }
        }

        if (total > 0) {
            steering.divide(total);
            steering.normalize();
            steering.multiply(maxSpeed);
            steering.subtract(velocity);
            steering.limit(maxForce);
        }
        return steering;
    }

    public void flock(ArrayList<Boid> boids, double cohesionWeight, double alignmentWeight, double separationWeight) {
        acceleration = new Vector3D(0, 0, 0);

        Vector3D alignment = alignment(boids);
        Vector3D cohesion = cohesion(boids);
        Vector3D separation = separation(boids);

        alignment.multiply(alignmentWeight);
        cohesion.multiply(cohesionWeight);
        separation.multiply(separationWeight);

        acceleration.add(alignment);
        acceleration.add(cohesion);
        acceleration.add(separation);

        System.out.println("Flock forces - Align: " + alignment +
                " Cohesion: " + cohesion + " Sep: " + separation);
    }

    public void update() {
        System.out.println("Updating boid position"); // Temporary debug
        position.add(velocity); //moves in the direction of the velocity
        velocity.add(acceleration); //changes the velocity based on the current acceleration
        velocity.limit(maxSpeed);
        acceleration.multiply(0); //it is diff for every frame
        System.out.println("After update - Pos: " + position + " Vel: " + velocity);
    }

    public void show(Graphics g) {
        // size depends on z (depth) — closer (higher z) means larger
//        int size = (int) Math.max(2, Math.min(10, position.z * 0.1)); // scale z from 0–100 to 2–10
//        System.out.println("Drawing boid at z=" + position.z + " with size=" + size);
//        g.fillOval((int) position.x - size / 2, (int) position.y - size / 2, size, size);

//        int size = 5;
//        g.fillOval((int)position.x - size/2, (int)position.y - size/2, size, size);

        // Size depends on z (depth) - closer (higher z) means larger
        // Scale z from 30-70 (based on your initialization) to size range 3-10
        int size = (int) Math.max(3, Math.min(10, 3 + (position.z - 30) * (7.0 / 40.0)));
        g.fillOval((int)position.x - size/2, (int)position.y - size/2, size, size);
    }
}
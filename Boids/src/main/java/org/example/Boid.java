package org.example;

import java.awt.Graphics;
import java.util.ArrayList;

public class Boid {
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    private double maxForce = 0.2;
    private double maxSpeed = 5;

    public Boid(double width, double height) {
        this.position = new Vector2D(Math.random() * width, Math.random() * height);
        this.velocity = Vector2D.random2D();
        this.velocity.multiply(2 + Math.random() *2);
        this.acceleration = new Vector2D(0, 0);
    }

    public void edges(double width, double height) {
        if(position.x > width) position.x = 0;
        else if(position.x < 0) position.x = width;

        if(position.y > height) position.y = 0;
        else if(position.y < 0) position.y = height;
    }

    public Vector2D alignment(ArrayList<Boid> boids) {
        double perceptionRadius = 25;
        Vector2D steering = new Vector2D(0, 0);
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

    public Vector2D cohesion(ArrayList<Boid> boids) {
        double perceptionRadius = 75;
        Vector2D steering = new Vector2D(0, 0);
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
    public Vector2D separation(ArrayList<Boid> boids) {
        double perceptionRadius = 50;
        Vector2D steering = new Vector2D(0, 0);
        int total = 0;

        for (Boid other : boids) {
            double d = position.distance(other.position);
            if (other != this && d < perceptionRadius) {
                Vector2D diff = Vector2D.subtract(position, other.position);    //direction in oposite
                diff.divide(d*d);   //closer the boid, stronger force to move
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
        acceleration = new Vector2D(0, 0);

        Vector2D alignment = alignment(boids);
        Vector2D cohesion = cohesion(boids);
        Vector2D separation = separation(boids);

        alignment.multiply(alignmentWeight);
        cohesion.multiply(cohesionWeight);
        separation.multiply(separationWeight);

        acceleration.add(alignment);
        acceleration.add(cohesion);
        acceleration.add(separation);
    }



    public void update() {
        position.add(velocity); //moves in the direction of the velocity
        velocity.add(acceleration); //changes the velocity based on the current acceleration
        acceleration.multiply(0);//it is diff for every frame
    }


    public void show(Graphics g) {
        g.fillOval((int) position.x - 4, (int) position.y - 4, 8, 8);
    }
}

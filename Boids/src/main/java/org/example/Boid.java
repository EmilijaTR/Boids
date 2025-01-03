package org.example;

import java.awt.Graphics;

public class Boid {
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    public Boid(double width, double height) {
        this.position = new Vector2D(width / 2, height / 2);
        this.velocity = Vector2D.random2D();
        this.acceleration = new Vector2D(0, 0);
    }
    public void update() {
        position.add(velocity); //moves in the direction of the velocity
        velocity.add(acceleration); //changes the velocity based on the current acceleration
    }
    public void show(Graphics g) {
        g.fillOval((int) position.x - 8, (int) position.y - 8, 16, 16); // Draw as a small circle
    }
}

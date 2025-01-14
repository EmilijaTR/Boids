package org.example;

import java.util.Random;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void subtract(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public static Vector2D subtract(Vector2D v1, Vector2D v2) { //i need it for separation!
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    public void divide(double scalar) {
        if (scalar != 0) {
            this.x /= scalar;
            this.y /= scalar;
        }
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public void normalize() {   //use it when want to preserve the direction only
        double magnitude = magnitude();
        if (magnitude != 0) {   //if the magnitude is zero, the vector has no direction
            this.x /= magnitude;
            this.y /= magnitude;
        }
    }

    public void limit(double max) { //use when you want to limit the speed
        if (magnitude() > max) {
            normalize();
            multiply(max);
        }
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);    //how far the vector is from (0,0), strength, size
    }

    public double distance(Vector2D other) {    //euclidean distance formula
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static Vector2D random2D(){
        Random r = new Random();
        double angle = r.nextDouble() * 2 * Math.PI;    //random from 0 to 360
        double magnitude = 0.5 + (1.5 - 0.5) * r.nextDouble();

        double x = Math.cos(angle) * magnitude;
        double y = Math.sin(angle) * magnitude;

        return new Vector2D(x,y);
    }
}

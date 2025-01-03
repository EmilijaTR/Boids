package org.example;

import java.util.Random;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }
    public static Vector2D random2D(){
        Random r = new Random();
        double angle = r.nextDouble() * 2 * Math.PI;
        double magnitude = 0.5 + (1.5 - 0.5) * r.nextDouble();

        double x = Math.cos(angle) * magnitude;
        double y = Math.sin(angle) * magnitude;

        return new Vector2D(x,y);
    }
}

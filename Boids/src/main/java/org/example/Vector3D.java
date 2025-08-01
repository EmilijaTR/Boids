package org.example;

import java.io.Serializable;
import java.util.Random;

public class Vector3D implements Serializable {
    private static final long serialVersionUID = 1L;

    public double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D copy() {
        return new Vector3D(this.x, this.y, this.z);
    }

    public void add(Vector3D other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void subtract(Vector3D other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public static Vector3D subtract(Vector3D v1, Vector3D v2) { // used for separation!
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public void divide(double scalar) {
        if (scalar != 0) {
            this.x /= scalar;
            this.y /= scalar;
            this.z /= scalar;
        }
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public void normalize() {   // use it when you want to preserve direction only
        double magnitude = magnitude();
        if (magnitude != 0) {   // if the magnitude is zero, the vector has no direction
            this.x /= magnitude;
            this.y /= magnitude;
            this.z /= magnitude;
        }
    }

    public void limit(double max) { // use when you want to limit the speed
        if (magnitude() > max) {
            normalize();
            multiply(max);
        }
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);    // how far the vector is from (0,0,0), strength, size
    }

    public double distance(Vector3D other) {    // Euclidean distance formula in 3D
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static Vector3D random3D(){
        Random r = new Random();

        // Generate a random point on a sphere and scale it with random magnitude
        double theta = r.nextDouble() * 2 * Math.PI;      // azimuth angle
        double phi = Math.acos(2 * r.nextDouble() - 1);    // polar angle
        double magnitude = 0.5 + (1.5 - 0.5) * r.nextDouble();  // magnitude from 0.5 to 1.5

        double x = Math.sin(phi) * Math.cos(theta) * magnitude;
        double y = Math.sin(phi) * Math.sin(theta) * magnitude;
        double z = Math.cos(phi) * magnitude;

        return new Vector3D(x, y, z);
    }
}

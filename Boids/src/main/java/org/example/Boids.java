package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Boids extends JPanel {
    private ArrayList<Boid> flock = new ArrayList<>();

    public Boids(int width, int height, BoidUpdater updater) {
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
    }

    public void setFlock(ArrayList<Boid> flock) {
        this.flock = flock;
    }

    public ArrayList<Boid> getFlock() {
        return flock;
    }



//    @Override
//    protected void paintComponent(Graphics g) {
////        super.paintComponent(g);
////        g.setColor(Color.BLACK);
////        g.fillRect(0, 0, getWidth(), getHeight());
////
////        g.setColor(Color.WHITE);
////        for (Boid boid : flock) {
////            boid.show(g);
////        }
//
//        super.paintComponent(g);
//        g.setColor(Color.BLACK);
//
//        // Debug: Count visible boids
//        int visible = 0;
//        for (Boid boid : flock) {
//            boid.show(g);
//            visible++;
//        }
//        System.out.println("Rendered " + visible + "/" + flock.size() + " boids");
//    }
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());

    g.setColor(Color.WHITE);
    for (Boid boid : flock) {
        boid.show(g);
    }
}
}

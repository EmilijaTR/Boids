package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Boids extends JPanel {
    private ArrayList<Boid> flock = new ArrayList<>();

    public Boids() {
        setPreferredSize(new Dimension(640, 360));
        for (int i = 0; i < 100; i++) {
            flock.add(new Boid(640, 360));

        }

        Timer timer = new Timer(16, e -> {
            for (Boid boid : flock) {
                boid.update(); //update the position of the boid
            }
            repaint();
        });
        timer.start();
    }

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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Boids");
        Boids simulation = new Boids();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(simulation);
        frame.pack();
        frame.setVisible(true);
    }
}

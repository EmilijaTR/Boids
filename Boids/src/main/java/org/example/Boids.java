package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Boids extends JPanel {
    private ArrayList<Boid> flock = new ArrayList<>();
    private JSlider cohesionSlider;
    private JSlider alignmentSlider;
    private JSlider separationSlider;

    private double cohesionWeight = 1.0;
    private double alignmentWeight = 1.0;
    private double separationWeight = 1.0;

    public Boids() {
        setPreferredSize(new Dimension(640, 400));
        setLayout(new BorderLayout());

        for (int i = 0; i < 100; i++) {
            flock.add(new Boid(640, 360));
        }

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(2, 3));
        sliderPanel.setOpaque(false);


        JLabel cohesionLabel = new JLabel("Cohesion", JLabel.CENTER);
        JLabel alignmentLabel = new JLabel("Alignment", JLabel.CENTER);
        JLabel separationLabel = new JLabel("Separation", JLabel.CENTER);

        cohesionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        alignmentSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        separationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        cohesionSlider.setBackground(new Color(0, 0, 0, 0));
        alignmentSlider.setBackground(new Color(0, 0, 0, 0));
        separationSlider.setBackground(new Color(0, 0, 0, 0));

        sliderPanel.add(cohesionLabel);
        sliderPanel.add(alignmentLabel);
        sliderPanel.add(separationLabel);

        sliderPanel.add(cohesionSlider);
        sliderPanel.add(alignmentSlider);
        sliderPanel.add(separationSlider);

        add(sliderPanel, BorderLayout.SOUTH);

        Timer timer = new Timer(16, e -> {  //60fps
            cohesionWeight = cohesionSlider.getValue() / 50.0; // Scale slider values
            alignmentWeight = alignmentSlider.getValue() / 50.0;
            separationWeight = separationSlider.getValue() / 50.0;

            for (Boid boid : flock) {
                boid.edges(640, 360);
                boid.flock(flock, cohesionWeight, alignmentWeight, separationWeight);
                boid.update();
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

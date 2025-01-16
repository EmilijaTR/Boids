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

    public Boids(int numBoids, int width, int height, double speed) {
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        for (int i = 0; i < numBoids; i++) {
            Boid boid = new Boid(width, height);
            Vector2D initialVelocity = boid.getVelocity();
            initialVelocity.normalize();
            initialVelocity.multiply(speed);
            boid.setVelocity(initialVelocity);
            flock.add(boid);
        }

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(2, 3));
        sliderPanel.setOpaque(false);


        JLabel cohesionLabel = new JLabel("Cohesion", JLabel.CENTER);
//        cohesionLabel.setForeground(Color.WHITE);
//        cohesionLabel.setFont(new Font("Arial", Font.BOLD, 12));
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

            cohesionWeight = cohesionSlider.getValue() / 50.0;
            alignmentWeight = alignmentSlider.getValue() / 50.0;
            separationWeight = separationSlider.getValue() / 50.0;

            //this is here bcs if the screen is resized the boids to move in the new dimensions
            int currentWidth = getWidth();
            int currentHeight = getHeight();

            for (Boid boid : flock) {
                boid.edges(currentWidth, currentHeight);
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
}
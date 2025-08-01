package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetupFrame extends JFrame {
    private BoidUpdater updater;
    private String title;

    // Constructor for all versions
    public SetupFrame(String title, BoidUpdater updater) {
        this.title = title;
        this.updater = updater;
        initializeUI();
    }

    private void initializeUI() {
        setTitle(title + " - Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 400);
        setMinimumSize(new Dimension(400, 500));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("BOIDS", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add input fields
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(createCustomLabel("Window Width:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(createCustomTextField("800"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(createCustomLabel("Window Height:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(createCustomTextField("600"), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(createCustomLabel("Number of Boids:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(createCustomTextField("100"), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(createCustomLabel("Initial Boid Speed:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(createCustomTextField("2.0"), gbc);

        // Start button
        JButton startButton = new JButton("Start Simulation");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        buttonPanel.add(startButton);

        // Add components to frame
        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listener
        startButton.addActionListener(e -> {
            try {
                int width = Integer.parseInt(((JTextField)inputPanel.getComponent(1)).getText());
                int height = Integer.parseInt(((JTextField)inputPanel.getComponent(3)).getText());
                int numBoids = Integer.parseInt(((JTextField)inputPanel.getComponent(5)).getText());
                double speed = Double.parseDouble(((JTextField)inputPanel.getComponent(7)).getText());

                dispose();
                startSimulation(numBoids, width, height, speed);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numerical values.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void startSimulation(int numBoids, int width, int height, double speed) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create appropriate simulation panel
        Boids boidsPanel = new Boids(width, height, updater);
        updater.setParameters(numBoids, width, height, speed);
        updater.setBoidsPanel(boidsPanel);
        frame.add(boidsPanel);

        // Add sliders
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(2, 3));
        sliderPanel.setOpaque(false);

        JLabel cohesionLabel = new JLabel("Cohesion", JLabel.CENTER);
        JLabel alignmentLabel = new JLabel("Alignment", JLabel.CENTER);
        JLabel separationLabel = new JLabel("Separation", JLabel.CENTER);

        JSlider cohesionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JSlider alignmentSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JSlider separationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

        sliderPanel.add(cohesionLabel);
        sliderPanel.add(alignmentLabel);
        sliderPanel.add(separationLabel);
        sliderPanel.add(cohesionSlider);
        sliderPanel.add(alignmentSlider);
        sliderPanel.add(separationSlider);

        frame.add(sliderPanel, BorderLayout.SOUTH);
        updater.setSliders(cohesionSlider, alignmentSlider, separationSlider);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);


        updater.start();
    }

    private JLabel createCustomLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JTextField createCustomTextField(String defaultValue) {
        JTextField textField = new JTextField(defaultValue);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.BLACK);
        textField.setBackground(new Color(220, 220, 220));
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textField.setCaretColor(Color.BLACK);
        return textField;
    }
}
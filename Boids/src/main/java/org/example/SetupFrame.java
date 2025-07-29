package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetupFrame extends JFrame {
    public SetupFrame() {
        setTitle("Setup Boids");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 400);
        setMinimumSize(new Dimension(400, 500));

        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        //title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("BOIDS", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);

        // create a panel for the input fields with some padding
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 15, 5); //add margin between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel widthLabel = createCustomLabel("Window Width:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(widthLabel, gbc);

        JTextField widthField = createCustomTextField("640");
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(widthField, gbc);

        JLabel heightLabel = createCustomLabel("Window Height:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(heightLabel, gbc);

        JTextField heightField = createCustomTextField("400");
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(heightField, gbc);

        JLabel numBoidsLabel = createCustomLabel("Number of Boids:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(numBoidsLabel, gbc);

        JTextField numBoidsField = createCustomTextField("100");
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(numBoidsField, gbc);

        JLabel speedLabel = createCustomLabel("Initial Boid Speed:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(speedLabel, gbc);

        JTextField speedField = createCustomTextField("3");
        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(speedField, gbc);

        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);

        JLabel modeLabel = createCustomLabel("Simulation Mode:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(modeLabel, gbc);

        String[] modes = {"Sequential", "Parallel", "Distributed"};
        JComboBox<String> modeComboBox = new JComboBox<>(modes);
        modeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(modeComboBox, gbc);

        JButton startButton = new JButton("Start Simulation");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(Color.WHITE);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numBoids = Integer.parseInt(numBoidsField.getText());
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());
                    double speed = Double.parseDouble(speedField.getText());
                    String selectedMode = (String) modeComboBox.getSelectedItem();

                    dispose(); //close setup

                    JFrame frame = new JFrame("Boids Simulation");
                    //Boids simulation = new Boids(numBoids, width, height, speed);
                    Boids simulation = new Boids(numBoids, width, height, speed, selectedMode);

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setMinimumSize(new Dimension(300, 300));
                    frame.add(simulation);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SetupFrame.this, "Please enter valid numbers for all fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SetupFrame().setVisible(true);
        });
    }
}

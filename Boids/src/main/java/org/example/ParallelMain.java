package org.example;

import javax.swing.SwingUtilities;

public class ParallelMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SetupFrame setup = new SetupFrame("Parallel Boids", new ParallelBoidUpdater());
            setup.setVisible(true);
        });
    }
}

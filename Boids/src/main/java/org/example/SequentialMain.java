package org.example;

import javax.swing.SwingUtilities;

public class SequentialMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SetupFrame setup = new SetupFrame("Sequential Boids", new SequentialBoidUpdater());
            setup.setVisible(true);
        });
    }
}

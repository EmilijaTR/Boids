package org.example;

import mpi.MPI;
import javax.swing.*;

public class DistributedMain {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (rank == 0) {
            // Only root process creates GUI
            SwingUtilities.invokeLater(() -> {
                DistributedBoidUpdater updater = new DistributedBoidUpdater();
                SetupFrame setup = new SetupFrame("Distributed Boids", updater);
                setup.setVisible(true);

                // Proper shutdown handling
                setup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setup.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        MPI.Finalize();
                        System.exit(0);
                    }
                });
            });
        } else {
            // Worker processes just run the updater without GUI
            DistributedBoidUpdater updater = new DistributedBoidUpdater();
            updater.start();
        }
    }
}
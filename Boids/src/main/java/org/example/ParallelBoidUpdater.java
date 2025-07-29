package org.example;

import java.util.ArrayList;

public class ParallelBoidUpdater {
    public static void update(ArrayList<Boid> flock, int width, int height,
                              double cohesionWeight, double alignmentWeight, double separationWeight) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = (int) Math.ceil((double) flock.size() / numThreads);

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = Math.min(flock.size(), start + chunkSize);

            Thread t = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    Boid boid = flock.get(j);
                    boid.edges(width, height);
                    boid.flock(flock, cohesionWeight, alignmentWeight, separationWeight);
                    boid.update();
                }
            });
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package org.example;

import javax.swing.*;

public interface BoidUpdater {
    void setParameters(int numBoids, int width, int height, double initialSpeed);
    void setSliders(JSlider cohesion, JSlider alignment, JSlider separation);
    void setBoidsPanel(Boids boidsPanel);
    void start();
}

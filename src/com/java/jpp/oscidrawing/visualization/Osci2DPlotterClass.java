package com.java.jpp.oscidrawing.visualization;

import com.java.jpp.oscidrawing.Signal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Osci2DPlotterClass implements Osci2DPlotter {
    BufferedImage image;
    int size;
    double scale;

    public Osci2DPlotterClass(BufferedImage image, int size, double scale) {
        this.image = image;
        this.size = size;
        this.scale = scale;
    }

    public Osci2DPlotterClass(int size, double scale, Color bgcol) {
        this.size = size;
        this.scale = scale;
        this.image = image;
    }

    @Override
    public int signalValToImageXCoord(double val) {
        double v = val + scale;
        double v1 = scale + scale;
        return (int) (((v * (size - 1)) / v1));
    }

    @Override
    public int signalValToImageYCoord(double val) {
        double v = val - scale;
        double v1 = -scale - scale;
        return (int) (((v * (size - 1)) / v1));
    }

    @Override
    public void drawSignalAt(Signal signal, int index, Color col) {
        if (signal.getChannelCount() != 2)
            throw new IllegalArgumentException();
        double x = signal.getValueAt(0, index);
        double y = signal.getValueAt(1, index);
        if (x >= 0 && x < size && y >= 0 && y < size)
            image.setRGB((int) x, (int) y, col.getRGB());
    }

    @Override
    public void drawSignal(Signal signal, Color col) {
        if (signal.isInfinite())
            throw new IllegalArgumentException();
        for (int i = 0; i < signal.getSize(); i++)
            drawSignalAt(signal, i, col);
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}

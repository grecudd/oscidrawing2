package com.java.jpp.oscidrawing.visualization;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.java.jpp.oscidrawing.Signal;

public interface Osci2DPlotter {
    public int signalValToImageXCoord(double val);

    public int signalValToImageYCoord(double val);

    public void drawSignalAt(Signal signal, int index, Color col);

    public void drawSignal(Signal signal, Color col);

    public BufferedImage getImage();

    public static Osci2DPlotter createImageCreator(int size, double scale, Color bgcol) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                image.setRGB(i, j, bgcol.getRGB());
            }
        return new Osci2DPlotterClass(image, size, scale);
    }
}

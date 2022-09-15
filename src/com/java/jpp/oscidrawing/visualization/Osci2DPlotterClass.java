package com.java.jpp.oscidrawing.visualization;

import com.java.jpp.oscidrawing.Signal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Osci2DPlotterClass implements Osci2DPlotter{
    BufferedImage image;
    int size;
    double scale;
    public Osci2DPlotterClass(BufferedImage image) {
        this.image = image;
    }

    public Osci2DPlotterClass(int size, double scale, Color bgcol) {
        this.size = size;
        this.scale = scale;
        image = Osci2DPlotter.createImageCreator(size, scale, bgcol).getImage();
    }

    @Override
    public int signalValToImageXCoord(double val) {
        return 0;
    }

    @Override
    public int signalValToImageYCoord(double val) {
        return 0;
    }

    @Override
    public void drawSignalAt(Signal signal, int index, Color col) {
        if(signal.getChannelCount() != 2)
            throw new IllegalArgumentException();

        /*image.setRGB(signal.getValueAtValid(signal.getValueAtValid(0), index),
                signal.getValueAtValid(1, 1),
                col);*/
    }

    @Override
    public void drawSignal(Signal signal, Color col) {
        if(signal.isInfinite() == true)
            throw new IllegalArgumentException();

        if(signal.getChannelCount() != 0)
            throw new IllegalArgumentException();

    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}

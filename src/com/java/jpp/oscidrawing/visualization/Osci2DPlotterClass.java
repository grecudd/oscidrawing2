package com.java.jpp.oscidrawing.visualization;

import com.java.jpp.oscidrawing.Signal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Osci2DPlotterClass implements Osci2DPlotter{

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

    }

    @Override
    public void drawSignal(Signal signal, Color col) {

    }

    @Override
    public BufferedImage getImage() {
        return null;
    }
}

package com.java.jpp.oscidrawing.visualization;

import com.java.jpp.oscidrawing.Signal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SignalTimePlotterClass implements SignalTimePlotter{
    int timeScale;
    int width;
    public SignalTimePlotterClass() {

    }

    @Override
    public int sampleIndexToImageXCoord(int sampleIndex, int sampleRate) {
        return ((sampleIndex-0)*(width-1-0))/((sampleRate*(timeScale-1)-0)-0);
    }

    @Override
    public int signalValToImageYCoord(double val) {
        return 0;
    }

    @Override
    public void drawSignalAt(Signal signal, int channel, int index, Color col) {

    }

    @Override
    public void drawSignal(Signal signal, int channel, Color col) {

    }

    @Override
    public void drawSignal(Signal signal, Color... colors) {

    }

    @Override
    public BufferedImage getImage() {
        return null;
    }
}
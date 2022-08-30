package com.java.jpp.oscidrawing.visualization;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.java.jpp.oscidrawing.Signal;

public interface SignalTimePlotter {

    public int sampleIndexToImageXCoord(int sampleIndex, int sampleRate);

    public int signalValToImageYCoord(double val);

    public void drawSignalAt(Signal signal, int channel, int index, Color col);

    public void drawSignal(Signal signal, int channel, Color col);

    public void drawSignal(Signal signal, Color... colors);

    public BufferedImage getImage();

    public static SignalTimePlotter createSignalTimePlotter(int width, int height, double valScale, double timeScale, Color bgcol, Color axiscol) {
        throw new UnsupportedOperationException();
    }
}

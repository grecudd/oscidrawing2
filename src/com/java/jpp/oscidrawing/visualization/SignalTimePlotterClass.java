package com.java.jpp.oscidrawing.visualization;

import com.java.jpp.oscidrawing.Signal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SignalTimePlotterClass implements SignalTimePlotter{
    double timeScale;
    int width;
    int height;
    double valScale;
    BufferedImage image;


    public SignalTimePlotterClass(double timeScale, int width,int height, double valScale, BufferedImage image) {
        this.timeScale = timeScale;
        this.width = width;
        this.height = height;
        this.valScale = valScale;
        this.image = image;
    }

    @Override
    public int sampleIndexToImageXCoord(int sampleIndex, int sampleRate) {
        return (int) ((sampleIndex*(width-1))/(sampleRate*timeScale-1));
    }

    @Override
    public int signalValToImageYCoord(double val) {
        return  (int) (((val-valScale)*(height-1))/(-valScale-valScale));
    }

    @Override
    public void drawSignalAt(Signal signal, int channel, int index, Color col) {
        double x = signal.getValueAt(channel, index);
        if (x >= 0 && x < width  && x < height)
            image.setRGB((int) x, (int) x, col.getRGB());
    }

    @Override
    public void drawSignal(Signal signal, int channel, Color col) {

    }

    @Override
    public void drawSignal(Signal signal, Color... colors) {

    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}

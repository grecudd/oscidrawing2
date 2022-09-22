package com.java.jpp.oscidrawing.visualization;

import com.java.jpp.oscidrawing.Signal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

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
        double x = signal.getValueAtValid(channel, index);
        if (x >= 0 && x < height)
            image.setRGB(index, (int) x, col.getRGB());
    }

    @Override
    public void drawSignal(Signal signal, int channel, Color col) {
        for(int index = 0; index < signal.getSize(); index++)
        {
            drawSignalAt(signal, channel, index, col);
        }
    }

    @Override
    public void drawSignal(Signal signal, Color... colors) {
        if(colors.length >= signal.getChannelCount())
            throw new IllegalArgumentException();

        List<Color> colorList = Arrays.stream(colors).toList();
        for(int channel = 0; channel < signal.getChannelCount(); channel++)
        {
            drawSignal(signal, channel, colorList.get(channel));
        }
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}

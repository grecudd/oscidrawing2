package com.java.jpp.oscidrawing.visualization;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.java.jpp.oscidrawing.Signal;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public interface SignalTimePlotter {

    public int sampleIndexToImageXCoord(int sampleIndex, int sampleRate);

    public int signalValToImageYCoord(double val);

    public void drawSignalAt(Signal signal, int channel, int index, Color col);

    public void drawSignal(Signal signal, int channel, Color col);

    public void drawSignal(Signal signal, Color... colors);

    public BufferedImage getImage();

    public static SignalTimePlotter createSignalTimePlotter(int width, int height, double valScale, double timeScale, Color bgcol, Color axiscol) throws Exception {
        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
        int middle = height / 2;

        if(height % 2 == 0){
            middle--;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(y == middle)
                {
                    image.setRGB(x, y, axiscol.getRGB());
                    continue;
                }
                image.setRGB(x, y, bgcol.getRGB());
            }
        }

        if(valScale != 0) {
            if(middle +  (int) valScale >= height)
            {
                throw new Exception("ValScare too big");
            }

            for (int x = 0; x < width; x++) {
                image.setRGB(x, middle +  (int) valScale, Color.WHITE.getRGB());
            }

            for (int x = 0; x < width; x++) {
                image.setRGB(x, middle -  (int) valScale, Color.WHITE.getRGB());
            }
        }

        return null;
    }
}

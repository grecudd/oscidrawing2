package com.java.jpp.oscidrawing;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.List;

public class SignalClass extends Signal {
    public SignalClass(List<List<Point>> values, int sampleRate, boolean infinte) {
        super(values, sampleRate, infinte);
    }

    @Override
    public boolean isInfinite() {
        return infinite;
    }

    @Override
    public int getSize() {

        if(isInfinite() == true)
            return -1;

        return points.get(0).size();
    }

    @Override
    public int getChannelCount() {
        return points.size();
    }

    @Override
    public int getSampleRate() {
        return sampleRate;
    }

    @Override
    public double getValueAtValid(int channel, int index) {
        return points.get(channel).get(index).getY();
    }

    @Override
    public double getValueAt(int channel, int index) {
        if(channel < 0 || channel >= getChannelCount())
            return 0;

        if(index < 0 || index >= getSize())
            return 0;

        return this.points.get(channel).get(index).getY();
    }
}

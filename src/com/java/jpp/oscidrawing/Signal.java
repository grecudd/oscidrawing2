package com.java.jpp.oscidrawing;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class Signal {

    public abstract boolean isInfinite();

    public abstract int getSize();

    public abstract int getChannelCount();

    public abstract int getSampleRate();

    public abstract double getValueAtValid(int channel, int index);

    public double getDuration() {
        if (isInfinite()) {
            return -1.0;
        }
        double duration = (double)getSize() / (double)getSampleRate();
        return duration;
    }

    public double getValueAt(int channel, int index) {
        if ((index >= getSize() || channel >= getChannelCount())||(index < 0 || channel < 0))
            return 0;
        return this.points.get(channel).get(index).getY();
    }
}

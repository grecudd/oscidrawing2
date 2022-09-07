package com.java.jpp.oscidrawing;

public abstract class Signal {
    public abstract boolean isInfinite();

    public abstract int getSize();

    public abstract int getChannelCount();

    public abstract int getSampleRate();

    public abstract double getValueAtValid(int channel, int index);

    public double getDuration() {
        if(isInfinite() == true)
        {
            return -1.0;
        }
        return 0;
    }

    public double getValueAt(int channel, int index) {
        throw new UnsupportedOperationException();
    }
}

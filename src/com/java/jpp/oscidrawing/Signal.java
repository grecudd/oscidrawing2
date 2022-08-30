package com.java.jpp.oscidrawing;

public abstract class Signal {
    public abstract boolean isInfinite();

    public abstract int getSize();

    public abstract int getChannelCount();

    public abstract int getSampleRate();

    public abstract double getValueAtValid(int channel, int index);

    public double getDuration() {throw new UnsupportedOperationException();}

    public double getValueAt(int channel, int index) {
        throw new UnsupportedOperationException();
    }
}

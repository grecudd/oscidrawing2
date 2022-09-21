package com.java.jpp.oscidrawing;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class Signal {
    List<List<Point>> points = new ArrayList<>();
    int sampleRate;
    boolean infinite;

    public Signal(List<List<Point>> values, int sampleRate, boolean infinte) {
        this.points = values;
        this.sampleRate = sampleRate;
        this.infinite = infinte;
    }


    public abstract boolean isInfinite();

    public abstract int getSize();

    public abstract int getChannelCount();

    public abstract int getSampleRate();

    public abstract double getValueAtValid(int channel, int index);

    public Signal(List<List<Point>> values, int sampleRate, boolean infinte){
        this.points = values;
        this.sampleRate = sampleRate;
        this.infinite = infinte;
    }

    public double getDuration() {
        if (isInfinite()) {
            return -1.0;
        }
        double duration = (double) getSize() / (double) getSampleRate();
        return duration;
    }


    public double getValueAt(int channel, int index) {

        if (channel < 0 || channel >= getChannelCount()) {

            return 0;
        }

        if (isInfinite()) {
            if (index < 0) {
                return 0;
            }
        } else {
            if (index < 0 || index >= getSize()) {
                return 0;
            }
        }
        return getValueAtValid(channel, index);
    }
}

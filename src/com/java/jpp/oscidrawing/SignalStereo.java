package com.java.jpp.oscidrawing;

import com.java.jpp.oscidrawing.Signal;
import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.ArrayList;
import java.util.List;

public class SignalStereo extends Signal {
    List<List<Point>> values = new ArrayList<>();
    int sampleRate;

    boolean infinite;
    public SignalStereo(List<List<Point>> values, int sampleRate){
        this.values = values;
        this.sampleRate = sampleRate;
        infinite = false;
    }

    public SignalStereo(){

    }

    public SignalStereo(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public SignalStereo(List<List<Point>> values, int sampleRate, boolean infinite) {
        this.values = values;
        this.sampleRate = sampleRate;
        this.infinite = infinite;
    }

    @Override
    public boolean isInfinite() {
        return infinite;
    }

    @Override
    public int getSize() {

        if(isInfinite() == true)
            return -1;

        return values.get(0).size();
    }

    @Override
    public int getChannelCount() {
        return values.size();
    }

    @Override
    public int getSampleRate() {
        return sampleRate;
    }

    @Override
    public double getValueAtValid(int channel, int index) {
        return values.get(channel).get(index).getY();
    }

    @Override
    public double getValueAt(int channel, int index) {
        if(channel < 0 || channel >= getChannelCount())
            return 0;

        if(index < 0 || index >= getSize())
            return 0;

        return this.values.get(channel).get(index).getY();
    }
}

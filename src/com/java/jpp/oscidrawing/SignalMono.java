package com.java.jpp.oscidrawing;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;

public class SignalMono extends Signal{
    List<Point> values = new ArrayList<>();
    int sampleRate;

    boolean infinite;
    public SignalMono(List<Point> values, int sampleRate){
        this.values = values;
        this.sampleRate = sampleRate;
        infinite = false;
    }

    public SignalMono(){

    }

    public SignalMono(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public SignalMono(List<Point> values, int sampleRate, boolean infinite) {
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
        {
            return -1;
        }
        return values.size();
    }

    @Override
    public int getChannelCount() {
        return 1;
    }

    @Override
    public int getSampleRate() {
        return sampleRate;
    }

    @Override
    public double getValueAtValid(int channel, int index) {
        return values.get(index).getY();
    }

    @Override
    public double getValueAt(int channel, int index) {
        if(channel < 0 || channel >= getChannelCount())
            return 0;

        if(index < 0 || index >= getSize())
            return 0;

        return this.values.get(index).getY();
    }
}

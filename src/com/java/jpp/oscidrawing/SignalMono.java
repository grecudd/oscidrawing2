package com.java.jpp.oscidrawing;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;

public class SignalMono extends Signal{
    List<Point> values = new ArrayList<>();
    int sampleRate;
    public SignalMono(final List<Point> values, int sampleRate){
        this.values = values;
        this.sampleRate = sampleRate;
    }
    @Override
    public boolean isInfinite() {
        return false;
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
}

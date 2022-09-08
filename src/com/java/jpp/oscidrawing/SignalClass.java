package com.java.jpp.oscidrawing;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.ArrayList;
import java.util.List;

public class SignalClass extends Signal{
    List<Point> values = new ArrayList<>();

    public SignalClass(final List<Point> values){
        this.values = values;
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
        return 0;
    }

    @Override
    public int getSampleRate() {
        return 0;
    }

    @Override
    public double getValueAtValid(int channel, int index) {
        return 0;
    }
}

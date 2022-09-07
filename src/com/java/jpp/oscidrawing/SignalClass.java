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
        double E = 0.0;
        for(Point val : values)
        {
            E += Math.pow(Math.abs(val.getY()), 2);
        }
        if(E != 0.0)
            return true;
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

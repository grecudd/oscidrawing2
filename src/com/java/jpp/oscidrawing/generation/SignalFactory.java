package com.java.jpp.oscidrawing.generation;

import com.java.jpp.oscidrawing.Signal;
import com.java.jpp.oscidrawing.SignalMono;
import com.java.jpp.oscidrawing.SignalStereo;
import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleUnaryOperator;

public abstract class SignalFactory {
    public static Signal fromValues(double[] signalData, int sampleRate) {
        if (sampleRate <= 0) throw new IllegalArgumentException();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < signalData.length; i++) {
            points.add(new Point(i, signalData[i]));
        }
        return new SignalMono(points, sampleRate);
    }

    public static Signal wave(DoubleUnaryOperator function, double frequency, double duration, int sampleRate) {
        if (frequency <= 0 || duration <= 0 || sampleRate <= 0) throw new IllegalArgumentException();
        double step = (frequency * 2.0 * Math.PI) / (double)sampleRate;
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < sampleRate * duration-1; i++) {
            points.add(new Point(i, function.applyAsDouble(i * step)));
        }
        return new SignalMono(points, sampleRate);
    }

    public static Signal rampUp(double duration, int sampleRate) {
        double samples = sampleRate * duration;
        if (duration <= 0 || sampleRate <= 0 || samples <= 2) throw new IllegalArgumentException();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < samples-1; i++) {
            points.add(new Point(i, i / (samples - 1)));
        }
        return new SignalMono(points, sampleRate);
    }

    public static Signal combineMonoSignals(List<Signal> signals) {
        if (signals == null) throw new NullPointerException();
        if (signals.size() <= 0) throw new IllegalArgumentException();
        int sampleRate = signals.get(0).getSampleRate();
        int size = Integer.MAX_VALUE;
        for (Signal signal : signals) {
            if (signal.getSampleRate() != sampleRate || signal.getChannelCount() != 1)
                throw new IllegalArgumentException();
            if (signal.getSize() < size)
                size = signal.getSize();
        }
        List<List<Point>> signal = new ArrayList<>();
        for (Signal value : signals) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                points.add(new Point(j, value.getValueAt(0, j)));
            }
            signal.add(points);
        }
        return new SignalStereo(signal, sampleRate);
    }

    public static Signal combineMonoSignals(Signal... signals) {
        Signal signal = combineMonoSignals(Arrays.stream(signals).toList());
        return signal;
    }

    public static Signal stereoFromMonos(Signal left, Signal right) {
        Signal signal = combineMonoSignals(Arrays.asList(left, right));
        return signal;
    }

    public static Signal extractChannels(Signal source, int... channels) {
        if (source == null)
            throw new NullPointerException();
        if (Arrays.stream(channels).anyMatch(z -> z < 0 || z > source.getChannelCount()))
            throw new IllegalArgumentException();
        List<List<Point>> signal = new ArrayList<>();
        for (int i = 0; i < channels.length; i++) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < source.getSize(); j++) {
                points.add(new Point(i, source.getValueAt(channels[i], j)));
            }
            signal.add(points);
        }
        return new SignalStereo(signal, source.getSampleRate());
    }

    public static Signal circle(double frequency, double duration, int sampleRate) {
        if (frequency <= 0 || duration <= 0 || sampleRate <= 0)
            throw new IllegalArgumentException();
        double step = (frequency * 2.0 * Math.PI) / (double)sampleRate;
        List<Point> sin = new ArrayList<>();
        List<Point> cos = new ArrayList<>();
        for (int i = 0; i < sampleRate * duration-1; i++) {
            sin.add(new Point(i, Math.sin(i * step)));
            cos.add(new Point(i, Math.cos(i * step)));
        }
        List<List<Point>> signal = new ArrayList<>();
        signal.add(sin);
        signal.add(cos);
        return new SignalStereo(signal, sampleRate);
    }

    public static Signal cycle(Signal signal) {
        if (signal == null)
            throw new NullPointerException();

        List<List<Point>> newSignal = new ArrayList<>();



        if(signal.getChannelCount() == 1)
            return new SignalMono(newSignal.get(0), signal.getSampleRate(), true);
        return new SignalStereo(newSignal, signal.getSampleRate(), true);
    }

    public static Signal infiniteFromValue(double value, int sampleRate) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1000; i += sampleRate) {
            points.add(new Point(i, value));
        }
        return new SignalMono(points, sampleRate, true);
    }

    public static Signal take(int count, Signal source) {
        if (count < 0)
            throw new IllegalArgumentException();
        List<List<Point>> signal = new ArrayList<>();
        for (int j = 0; j < source.getChannelCount(); j++) {
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < source.getSize(); i++) {
                if (source.getSize() > count) {
                    points.add(new Point(i, source.getValueAtValid(j, i)));
                } else {
                    if (i >= count) {
                        points.add(new Point(i, source.getValueAtValid(j, 0)));
                    } else
                        points.add(new Point(i, source.getValueAtValid(j, i)));
                }
            }
            signal.add(points);
        }
        return new SignalStereo(signal, source.getSampleRate());
    }

    public static Signal drop(int count, Signal source) {
        if(source == null)
        {
            throw new NullPointerException();
        }

        if (count <= 0) {
            throw new IllegalArgumentException();
        }

        if (source.isInfinite() == false) {
            //if count >= size return empty signal
            if (count >= source.getSize()) {
                if (source.getChannelCount() == 1) {
                    return new SignalMono(source.getSampleRate());
                } else return new SignalStereo(source.getSampleRate());
            } else {
                List<List<Point>> signal = new ArrayList<>();
                for (int channel = 0; channel < source.getChannelCount(); channel++) {
                    List<Point> points = new ArrayList<>();
                    for (int index = count; index < source.getSize(); index++) {
                        points.add(new Point(channel, source.getValueAtValid(channel, index)));
                    }

                    signal.add(points);
                }

                if(signal.size() == 0){
                    if(source.getChannelCount() == 1)
                        return new SignalMono();
                    return new SignalStereo();
                }

                if (source.getChannelCount() == 1)
                    return new SignalMono(signal.get(0), source.getSampleRate());
                return new SignalStereo(signal, source.getSampleRate());
            }
        }

        return source;
    }

    public static Signal transform(DoubleUnaryOperator function, Signal source) {
        if (function == null)
            throw new NullPointerException();

        if (source == null)
            throw new NullPointerException();

        List<List<Point>> signal = new ArrayList<>();
        for (int channel = 0; channel < source.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();
            for (int index = 0; index < source.getSize(); index++) {
                points.add(new Point(channel, function.applyAsDouble(source.getValueAt(channel, index))));
            }
            signal.add(points);
        }

        if (source.getChannelCount() == 1)
            return new SignalMono(signal.get(0), source.getSampleRate(), source.isInfinite());
        return new SignalStereo(signal, source.getSampleRate(), source.isInfinite());
    }

    public static Signal scale(double amplitude, Signal source) {
        if (source == null)
            throw new NullPointerException("Null source ");

        List<List<Point>> signal = new ArrayList<>();
        for (int channel = 0; channel < source.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();
            for (int index = 0; index < source.getSize(); index++) {
                points.add(new Point(channel, source.getValueAt(channel, index) * amplitude));
            }
            signal.add(points);
        }

        if (source.getChannelCount() == 1)
            return new SignalMono(signal.get(0), source.getSampleRate(), source.isInfinite());
        return new SignalStereo(signal, source.getSampleRate(), source.isInfinite());
    }

    public static Signal reverse(Signal source) {
        if (source == null)
            throw new NullPointerException();

        if (source.isInfinite() == true)
            throw new IllegalArgumentException();

        List<List<Point>> signal = new ArrayList<>();
        for (int channel = 0; channel < source.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();
            for (int index = 0; index < source.getSize(); index++) {
                points.add(new Point(channel, source.getValueAt(channel, source.getSize() - 1 - index)));
            }
            signal.add(points);
        }

        if(signal.size() == 0){
            if(source.getChannelCount() == 1)
                return new SignalMono();
            return new SignalStereo();
        }

        if (source.getChannelCount() == 1)
            return new SignalMono(signal.get(0), source.getSampleRate());
        return new SignalStereo(signal, source.getSampleRate());

    }

    public static Signal rampDown(double duration, int sampleRate) {
        double samples = sampleRate * duration;
        if (duration <= 0 || sampleRate <= 0 || samples <= 2) throw new IllegalArgumentException();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < samples; i++) {
            points.add(new Point(i, i / (samples - 1)));
        }
        Collections.reverse(points);
        return new SignalMono(points, sampleRate);
    }

    public static Signal merge(BiFunction<Double, Double, Double> function, Signal s1, Signal s2) {
        if (s1 == null || s2 == null || function == null)
            throw new NullPointerException();

        if (s1.getSampleRate() != s2.getSampleRate())
            throw new IllegalArgumentException();

        if (s1.getChannelCount() != s2.getChannelCount())
            throw new IllegalArgumentException();

        int size = Math.min(s1.getSize(), s2.getSize());

        List<List<Point>> values = new ArrayList<>();

        for (int channel = 0; channel < s1.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();

            for (int index = 0; index < size; index++) {
                points.add(new Point(channel,
                        function.apply(s1.getValueAt(channel, index), s2.getValueAt(channel, index))));
            }

            values.add(points);
        }
        if(values.size() == 0){
            if(s1.getChannelCount() == 1)
                return new SignalMono(s1.getSampleRate());
            return new SignalStereo(s1.getSampleRate());
        }
        if(s1.getChannelCount() == 1)

            return new SignalMono(values.get(0), s1.getSampleRate());
        return new SignalStereo(values, s1.getSampleRate());
    }

    public static Signal add(Signal s1, Signal s2) {
        if (s1 == null || s2 == null)
            throw new NullPointerException();

        if (s1.getSampleRate() != s2.getSampleRate())
            throw new IllegalArgumentException();

        if (s1.getChannelCount() != s2.getChannelCount())
            throw new IllegalArgumentException();

        int size = Math.min(s1.getSize(), s2.getSize());

        List<List<Point>> values = new ArrayList<>();

        for (int channel = 0; channel < s1.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();

            for (int index = 0; index < size; index++) {
                points.add(new Point(channel,
                        s1.getValueAt(channel, index) + s2.getValueAt(channel, index)));
            }

            values.add(points);
        }


        if(values.size() == 0){
            if(s1.getChannelCount() == 1)
                return new SignalMono(s1.getSampleRate());
            return new SignalStereo(s1.getSampleRate());
        }

        if(s1.getChannelCount() == 1)
            return new SignalMono(values.get(0), s1.getSampleRate());
        return new SignalStereo(values, s1.getSampleRate());
    }

    public static Signal mult(Signal s1, Signal s2) {
        if (s1 == null || s2 == null)
            throw new NullPointerException();

        if (s1.getSampleRate() != s2.getSampleRate())
            throw new IllegalArgumentException();

        if (s1.getChannelCount() != s2.getChannelCount())
            throw new IllegalArgumentException();

        int size = Math.min(s1.getSize(), s2.getSize());

        List<List<Point>> values = new ArrayList<>();

        for (int channel = 0; channel < s1.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();

            for (int index = 0; index < size; index++) {
                points.add(new Point(channel,
                        s1.getValueAt(channel, index) * s2.getValueAt(channel, index)));
            }

            values.add(points);
        }

        if(values.size() == 0){
            if(s1.getChannelCount() == 1)
                return new SignalMono(s1.getSampleRate());
            return new SignalStereo(s1.getSampleRate());
        }

        if(s1.getChannelCount() == 1)
            return new SignalMono(values.get(0), s1.getSampleRate());
        return new SignalStereo(values, s1.getSampleRate());
    }

    public static Signal append(List<Signal> signals) {
        if(signals == null)
            throw new NullPointerException();

        if(signals.size() == 0)
            throw new IllegalArgumentException();
        int sampleRate = signals.get(0).getSampleRate();
        int channelCount = signals.get(0).getChannelCount();

        boolean infinite = false;
        for(int i = 1; i < signals.size(); i++){
            if(signals.get(i).getSampleRate() != sampleRate)
                throw new IllegalArgumentException();

            if(signals.get(i).getChannelCount() != channelCount)
                throw new IllegalArgumentException();

            if(signals.get(i).isInfinite() && i!=signals.size()-1){
                    throw new IllegalArgumentException();
                else infinite = true;
            }
        }

        List<Point> signal = new ArrayList<>();
        for(int channel = 0; channel < signals.size(); channel++)
        {
            for(int index = 0; index < signals.get(0).getSize(); index++)
            {
                signal.add(new Point(channel * signals.get(channel).getChannelCount() + index,
                        signals.get(channel).getValueAtValid(0, index)));
            }
        }

        return new SignalMono(signal, signals.get(0).getSampleRate(), infinite);
    }

    public static Signal append(Signal... signals) {
        Signal signal = append(Arrays.stream(signals).toList());
        return signal;
    }

    public static Signal translate(List<Double> distances, Signal signal) {
        if(distances == null)
            throw new NullPointerException();

        if(signal == null)
            throw new NullPointerException();

        if(distances.size() != signal.getChannelCount())
            throw new IllegalArgumentException();

        List<List<Point>> newSignal = new ArrayList<>();

        for(int channel = 0; channel < signal.getChannelCount(); channel++){
            List<Point> points = new ArrayList<>();

            for(int index = 0; index < signal.getSize(); index++){
                points.add(new Point(channel, signal.getValueAtValid(channel, index) + distances.get(channel)));
            }

            newSignal.add(points);
        }

        if(newSignal.size() == 0){
            if(signal.getChannelCount() == 1)
                return new SignalMono(signal.getSampleRate());
            return new SignalStereo(signal.getSampleRate());
        }

        if(signal.getChannelCount() == 1)
            return new SignalMono(newSignal.get(0), signal.getSampleRate());
        return new SignalStereo(newSignal, signal.getSampleRate());
    }

    public static Signal fromPath(List<Point> points, double frequency, int sampleRate) {
        if(frequency <= 0 || sampleRate <= 0)
            throw new IllegalArgumentException();

        if(points.size() < 2)
            throw new IllegalArgumentException();

        return null;
    }

    /* Optional */
    public static Signal myCoolSignal() {
        return null;
    }
}

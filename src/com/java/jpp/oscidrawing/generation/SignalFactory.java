package com.java.jpp.oscidrawing.generation;

import com.java.jpp.oscidrawing.Signal;
import com.java.jpp.oscidrawing.SignalClass;
import com.java.jpp.oscidrawing.generation.pathutils.Line;
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

        List<List<Point>> signal = new ArrayList<>();
        signal.add(points);
        return new SignalClass(signal, sampleRate, false);
    }

    public static Signal wave(DoubleUnaryOperator function, double frequency, double duration, int sampleRate) {
        if (frequency <= 0 || duration <= 0 || sampleRate <= 0) throw new IllegalArgumentException();
        double step = (frequency * 2.0 * Math.PI) / (double) sampleRate;
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < sampleRate * duration; i++) {
            points.add(new Point(i, function.applyAsDouble(i * step)));
        }
        List<List<Point>> signal = new ArrayList<>();
        signal.add(points);
        return new SignalClass(signal, sampleRate, false);
    }

    public static Signal rampUp(double duration, int sampleRate) {
        double samples = sampleRate * duration;
        if (duration <= 0 || sampleRate <= 0 || samples <= 2) throw new IllegalArgumentException();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < samples - 1; i++) {
            points.add(new Point(i, i / (samples - 1)));
        }
        List<List<Point>> signal = new ArrayList<>();
        signal.add(points);
        return new SignalClass(signal, sampleRate, false);
    }

    public static Signal combineMonoSignals(List<Signal> signals) {
        if (signals == null) throw new NullPointerException();
        if (signals.size() <= 0) throw new IllegalArgumentException();
        int sampleRate = signals.get(0).getSampleRate();
        int size = Integer.MAX_VALUE;
        int infinite = 0;
        for (Signal signal : signals) {
            if (signal.getSampleRate() != sampleRate || signal.getChannelCount() != 1)
                throw new IllegalArgumentException();
            if (signal.getSize() < size)
                size = signal.getSize();

            if (signal.isInfinite())
                infinite++;
        }
        List<List<Point>> signal = new ArrayList<>();
        for (Signal value : signals) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                points.add(new Point(j, value.getValueAt(0, j)));
            }
            signal.add(points);
        }
        return new SignalClass(signal, sampleRate, infinite == signals.size() ? true : false);
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
        if (Arrays.stream(channels).anyMatch(z -> z < 0 || z >= source.getChannelCount()))
            throw new IllegalArgumentException();

        List<List<Point>> signal = new ArrayList<>();
        for (int i = 0; i < channels.length; i++) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < source.getSize(); j++) {
                points.add(new Point(i, source.getValueAt(channels[i], j)));
            }
            signal.add(points);
        }
        return new SignalClass(signal, source.getSampleRate(), false);
    }

    public static Signal circle(double frequency, double duration, int sampleRate) {
        if (frequency <= 0 || duration <= 0 || sampleRate <= 0)
            throw new IllegalArgumentException();
        double step = (frequency * 2.0 * Math.PI) / (double) sampleRate;
        List<Point> sin = new ArrayList<>();
        List<Point> cos = new ArrayList<>();
        for (int i = 0; i < sampleRate * duration - 1; i++) {
            sin.add(new Point(i, Math.sin(i * step)));
            cos.add(new Point(i, Math.cos(i * step)));
        }
        List<List<Point>> signal = new ArrayList<>();
        signal.add(sin);
        signal.add(cos);
        return new SignalClass(signal, sampleRate, false);
    }

    public static Signal cycle(Signal signal) {
        if (signal == null)
            throw new NullPointerException();

        if (signal.isInfinite())
            return signal;

        List<List<Point>> newSignal = new ArrayList<>();

        for (int channel = 0; channel < signal.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();
            for (int index = 0; index < signal.getSize(); index++) {
                points.add(new Point(index, signal.getValueAt(channel, index)));
            }
            newSignal.add(points);
        }

        return new SignalClass(newSignal, signal.getSampleRate(), true);
    }

    public static Signal infiniteFromValue(double value, int sampleRate) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1000; i += sampleRate) {
            points.add(new Point(i, value));
        }
        List<List<Point>> newSignal = new ArrayList<>();
        newSignal.add(points);
        return new SignalClass(newSignal, sampleRate, true);
    }

    public static Signal take(int count, Signal source) {
        if (count < 0)
            throw new IllegalArgumentException();
        List<List<Point>> signal = new ArrayList<>();
        for (int j = 0; j < source.getChannelCount(); j++) {
            List<Point> points = new ArrayList<>();
            int i = 0;
            for (; i < source.getSize(); i++) {
                points.add(new Point(i, source.getValueAtValid(j, i)));
            }

            for (; i < count; i++) {
                points.add(new Point(i, 0));
            }

            signal.add(points);
        }
        return new SignalClass(signal, source.getSampleRate(), false);
    }

    public static Signal drop(int count, Signal source) {
        if (source == null) {
            throw new NullPointerException();
        }

        if (count < 0 || count >= source.getSize()) {
            throw new IllegalArgumentException();
        }

        if (source.isInfinite() == false) {
            if (count >= source.getSize()) {
                return new SignalClass(null, source.getSampleRate(), false);
            } else {
                List<List<Point>> signal = new ArrayList<>();
                for (int channel = 0; channel < source.getChannelCount(); channel++) {
                    List<Point> points = new ArrayList<>();
                    for (int index = count; index < source.getSize(); index++) {
                        points.add(new Point(channel, source.getValueAtValid(channel, index)));
                    }

                    signal.add(points);
                }

                return new SignalClass(signal, source.getSampleRate(), source.isInfinite());
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

        return new SignalClass(signal, source.getSampleRate(), source.isInfinite());
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

        return new SignalClass(signal, source.getSampleRate(), source.isInfinite());
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

        return new SignalClass(signal, source.getSampleRate(), source.isInfinite());

    }

    public static Signal rampDown(double duration, int sampleRate) {
        double samples = sampleRate * duration - 1;
        if (duration <= 0 || sampleRate <= 0 || samples <= 2) throw new IllegalArgumentException();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < samples; i++) {
            points.add(new Point(i, i / (samples - 1)));
        }
        Collections.reverse(points);

        List<List<Point>> values = new ArrayList<>();
        values.add(points);
        return new SignalClass(values, sampleRate, false);
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

        return new SignalClass(values, s1.getSampleRate(), s1.isInfinite());
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

        return new SignalClass(values, s1.getSampleRate(), s1.isInfinite());
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

        return new SignalClass(values, s1.getSampleRate(), s1.isInfinite());
    }

    public static Signal append(List<Signal> signals) {
        if (signals == null)
            throw new NullPointerException();

        if (signals.size() == 0)
            throw new IllegalArgumentException();
        int sampleRate = signals.get(0).getSampleRate();
        int channelCount = signals.get(0).getChannelCount();

        boolean infinite = false;
        for (int i = 1; i < signals.size(); i++) {
            if (signals.get(i).getSampleRate() != sampleRate)
                throw new IllegalArgumentException();

            if (signals.get(i).getChannelCount() != channelCount)
                throw new IllegalArgumentException();

            if (signals.get(i).isInfinite()) {
                if (i != signals.size() - 1)
                    throw new IllegalArgumentException();
                else infinite = true;
            }
        }

        List<Point> signal = new ArrayList<>();
        for (int channel = 0; channel < signals.size(); channel++) {
            for (int index = 0; index < signals.get(channel).getSize(); index++) {
                signal.add(new Point(channel * signals.get(channel).getChannelCount() + index,
                        signals.get(channel).getValueAtValid(0, index)));
            }
        }
        List<List<Point>> values = new ArrayList<>();
        values.add(signal);
        return new SignalClass(values, signals.get(0).getSampleRate(), infinite);
    }

    public static Signal append(Signal... signals) {
        Signal signal = append(Arrays.stream(signals).toList());
        return signal;
    }

    public static Signal translate(List<Double> distances, Signal signal) {
        if (distances == null)
            throw new NullPointerException();

        if (signal == null)
            throw new NullPointerException();

        if (distances.size() != signal.getChannelCount())
            throw new IllegalArgumentException();

        List<List<Point>> newSignal = new ArrayList<>();

        for (int channel = 0; channel < signal.getChannelCount(); channel++) {
            List<Point> points = new ArrayList<>();

            for (int index = 0; index < signal.getSize(); index++) {
                points.add(new Point(channel, signal.getValueAtValid(channel, index) + distances.get(channel)));
            }

            newSignal.add(points);
        }

        return new SignalClass(newSignal, signal.getSampleRate(), signal.isInfinite());
    }

    public static Signal fromPath(List<Point> points, double frequency, int sampleRate) {
        if (frequency <= 0 || sampleRate <= 0)
            throw new IllegalArgumentException();

        if (points.size() < 2)
            throw new IllegalArgumentException();

        double duration = (double) points.size() / (double) sampleRate;

        List<Line> lines = new ArrayList<>();
        List<Double> lineLengths = new ArrayList<>();
        double pathLength = 0;
        for (int i = 1; i < points.size(); i++) {
            lines.add(new Line(points.get(i - 1), points.get(i)));
            double len = lines.get(i - 1).length();
            lineLengths.add(len);
            pathLength += len;
        }

        List<Double> normalizedLineLenghts = new ArrayList<>();
        List<Integer> pointsPerLine = new ArrayList<>();
        for (int i = 0; i < lineLengths.size(); i++) {
            normalizedLineLenghts.add((double) (lineLengths.get(i) / pathLength));
            pointsPerLine.add((int) (duration * normalizedLineLenghts.get(i) * sampleRate));
        }

        List<Point> interpolatedPoints = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            int numPoints = pointsPerLine.get(i);
            List<Integer> indices = new ArrayList<>();

            for (int j = 0; j < numPoints; j++) {
                indices.add(j);
            }

            List<Double> lineProgress = new ArrayList<>();

            for (int j = 0; j < numPoints; j++) {
                lineProgress.add((double) indices.get(j) / (double) numPoints);
            }

            List<Point> interpolatedPointsOfLine = new ArrayList<>();

            for (int j = 0; j < lineProgress.size(); j++) {
                interpolatedPointsOfLine.add(lines.get(j).getPointAt(lineProgress.get(j)));
                interpolatedPoints.add(interpolatedPointsOfLine.get(j));
            }
        }

        List<List<Point>> signal = new ArrayList<>();
        List<Point> values = new ArrayList<>();
        List<Point> values1 = new ArrayList<>();
        for (int index = 0; index < interpolatedPoints.size(); index++) {
            values.add(new Point(index, interpolatedPoints.get(index).getX()));
            values1.add(new Point(index, interpolatedPoints.get(index).getY()));
        }

        signal.add(values);
        signal.add(values1);

        return new SignalClass(signal, sampleRate, false);

    }

    /* Optional */
    public static Signal myCoolSignal() {
        return null;
    }
}

package com.java.jpp.oscidrawing.generation;

import com.java.jpp.oscidrawing.Signal;
import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleUnaryOperator;

public abstract class SignalFactory {
    public static Signal fromValues(double[] signalData, int sampleRate) {
        throw new UnsupportedOperationException();
    }

    public static Signal wave(DoubleUnaryOperator function, double frequency, double duration, int sampleRate) {
        throw new UnsupportedOperationException();
    }

    public static Signal rampUp(double duration, int sampleRate) {
        throw new UnsupportedOperationException();
    }

    public static Signal combineMonoSignals(List<Signal> signals) {
        throw new UnsupportedOperationException();
    }

    public static Signal combineMonoSignals(Signal... signals) {
        throw new UnsupportedOperationException();
    }

    public static Signal stereoFromMonos(Signal left, Signal right) {
        throw new UnsupportedOperationException();
    }

    public static Signal extractChannels(Signal source, int... channels) {
        throw new UnsupportedOperationException();
    }

    public static Signal circle(double frequency, double duration, int sampleRate) {
        throw new UnsupportedOperationException();
    }

    public static Signal cycle(Signal signal) {
        throw new UnsupportedOperationException();
    }

    public static Signal infiniteFromValue(double value, int sampleRate) {
        throw new UnsupportedOperationException();
    }

    public static Signal take(int count, Signal source) {
        throw new UnsupportedOperationException();
    }

    public static Signal drop(int count, Signal source) {
        throw new UnsupportedOperationException();
    }

    public static Signal transform(DoubleUnaryOperator function, Signal source) {
        throw new UnsupportedOperationException();
    }

    public static Signal scale(double amplitude, Signal source) {
        throw new UnsupportedOperationException();
    }

    public static Signal reverse(Signal source) {
        throw new UnsupportedOperationException();
    }

    public static Signal rampDown(double duration, int sampleRate) {
        throw new UnsupportedOperationException();
    }

    public static Signal merge(BiFunction<Double, Double, Double> function, Signal s1, Signal s2) {
        throw new UnsupportedOperationException();
    }

    public static Signal add(Signal s1, Signal s2) {
        throw new UnsupportedOperationException();
    }

    public static Signal mult(Signal s1, Signal s2) {
        throw new UnsupportedOperationException();
    }

    public static Signal append(List<Signal> signals) {
        throw new UnsupportedOperationException();
    }

    public static Signal append(Signal... signals) {
        throw new UnsupportedOperationException();
    }

    public static Signal translate(List<Double> distances, Signal signal) {
        throw new UnsupportedOperationException();
    }

    public static Signal fromPath(List<Point> points, double frequency, int sampleRate) {
        throw new UnsupportedOperationException();
    }

    /* Optional */
    public static Signal myCoolSignal() {
        return null;
    }
}

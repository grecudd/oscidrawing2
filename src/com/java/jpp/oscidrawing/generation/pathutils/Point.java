package com.java.jpp.oscidrawing.generation.pathutils;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point p) {
        return Math.sqrt((p.getY() - y) * (p.getY() - y) + (p.getX() - x) * (p.getX() - x));
    }

    @Override
    public String toString() {
        return "Point{x=" + x + ", y=" + y + "}";
    }

    public Point interpolateTo(Point p, double factor) {
        if (factor == 0) {
            return this;
        }
        if (factor == 1) {
            return p;
        }

        if(getY() == p.getY() && getY() == p.getY())
            return this;

        return new Point(x + factor * p.getX(), y + factor * p.getY());
    }
}

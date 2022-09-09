package com.java.jpp.oscidrawing.generation.pathutils;

public class Line {
    private final Point p1;
    private final Point p2;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getStart() {
        return p1;
    }

    public Point getEnd() {
        return p2;
    }

    public double length() {
        return p1.distanceTo(p2);
    }

    public Point getPointAt(double percentage) {
        if(p1.getX() == p2.getX() && p1.getY() == p2.getY()){
            return new Point(p1.getX(), p1.getY());
        }
        if (percentage == 0) {
            return p1;
        }
        if (percentage == 1) {
            return p2;
        }
//        percentage = percentage / 100.0;
        return p1.interpolateTo(p2, percentage);
    }

    @Override
    public String toString() {
        return "Line{p1=" + p1.toString() + ", p2=" + p2.toString() + "}";
    }
}

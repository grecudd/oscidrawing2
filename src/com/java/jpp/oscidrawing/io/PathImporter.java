package com.java.jpp.oscidrawing.io;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathImporter {
    public static Optional<List<Point>> fromString(List<String> lines) {
        List<Point> points = new ArrayList<>();

        for(String line : lines){
            String[] numbers = line.split(",");
            double x, y;

            try {
                x = Double.valueOf(numbers[0]);
                y = Double.valueOf(numbers[1]);
            } catch (Exception e){
                continue;
            }

            points.add(new Point(x, y));
        }

        return Optional.of(points);
    }

    public static Optional<List<Point>> fromFile(String path) {
        throw new UnsupportedOperationException();
    }
}

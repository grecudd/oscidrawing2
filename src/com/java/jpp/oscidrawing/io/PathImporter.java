package com.java.jpp.oscidrawing.io;

import com.java.jpp.oscidrawing.generation.pathutils.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathImporter {
    public static Optional<List<Point>> fromString(List<String> lines) {
        List<Point> points = new ArrayList<>();
        try {
            for (String line : lines) {
                String[] numbers = line.split(",");
                double x, y;

                if(numbers.length != 2)
                    throw new IllegalArgumentException();

                x = Double.valueOf(numbers[0]);
                y = Double.valueOf(numbers[1]);

                points.add(new Point(x, y));
            }


            return Optional.of(points);
        } catch (Exception e) {
            return Optional.of(points);
        }
    }

    public static Optional<List<Point>> fromFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            List<String> lines = new ArrayList<>();
            do {
                line = reader.readLine();
                lines.add(line);
            } while (line != null);
            reader.close();
            return fromString(lines);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

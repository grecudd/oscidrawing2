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

        try {
            return Optional.of(points);
        }catch(Exception e){
            return Optional.empty();
        }
    }

    public static Optional<List<Point>> fromFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            List<String> lines = new ArrayList<>();
            do{
                line = reader.readLine();
                lines.add(line);
            } while (line != null);
            reader.close();
            return fromString(lines);
        } catch(Exception e){
            return Optional.empty();
        }
    }
}

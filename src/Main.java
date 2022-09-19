import com.java.jpp.oscidrawing.SignalClass;
import com.java.jpp.oscidrawing.generation.pathutils.Line;
import com.java.jpp.oscidrawing.generation.pathutils.Point;
import com.java.jpp.oscidrawing.io.AudioExporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        pointTest();
//        lineTest();
        List<List<Point>> list = new ArrayList<>();
        List<Point> points = Arrays.asList(new Point(2, 1), new Point(7, 3));
        list.add(points);
        boolean b=true;
        try {
            System.out.println(AudioExporter.writeChannelToFile("C://temp/signal", new SignalClass(list, 1), 0));
        } catch (IllegalAccessException e) {

        }

    }

    public static void lineTest() {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Line l = new Line(p1, p2);

        System.out.println(l.getStart());
        System.out.println(l.getEnd());
        System.out.println(l.getPointAt(20).toString());
        System.out.println(l);
    }

    public static void pointTest() {
        Point p = new Point(1, 1);
        Point p2 = new Point(1, 1);

        System.out.println(p.distanceTo(p2));
        System.out.println(p);
        System.out.println(p.interpolateTo(p2, 0).toString());
        System.out.println(p.interpolateTo(p2, 1).toString());
        System.out.println(p.interpolateTo(p2, 2).toString());
    }
}

import com.java.jpp.oscidrawing.Signal;
import com.java.jpp.oscidrawing.SignalClass;
import com.java.jpp.oscidrawing.generation.SignalFactory;
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
//        List<List<Point>> list = new ArrayList<>();
//        List<Point> points = Arrays.asList(new Point(2, 1), new Point(7, 3));
//        list.add(points);
//        boolean b=true;
//        try {
//            System.out.println(AudioExporter.writeChannelToFile("C://temp/signal", new SignalClass(list, 1), 0));
//        } catch (IllegalAccessException e) {
//
//        }
        Signal signal = SignalFactory.fromPath(List.of(new Point
                (0, 0), new Point(1, -1), new Point(1, 0.5),
                new Point(0.4, 0.6)), 1.000000, 100);
        System.out.print(signal.getSize());
    }
}

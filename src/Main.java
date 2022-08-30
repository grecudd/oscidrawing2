import com.java.jpp.oscidrawing.generation.pathutils.Point;

public class Main {
    public static void main(String[] args) {
        Point p = new Point(1, 1);
        Point p2 = new Point(1, 1);

        System.out.println(p.distanceTo(p2));
        System.out.println(p.toString());
        System.out.println(p.interpolateTo(p2, 2).toString());
    }
}

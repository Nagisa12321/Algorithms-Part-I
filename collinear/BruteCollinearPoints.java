import java.util.ArrayList;
import java.util.List;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/5/16 17:04
 */
public class BruteCollinearPoints {
    private final List<LineSegment> segments;

    // the line segments
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        segments = new ArrayList<>();

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        Point point1 = points[i];
                        Point point2 = points[j];
                        Point point3 = points[k];
                        Point point4 = points[m];
                        Point[] pts = { point1, point2, point3, point4 };

                        for (Point p : pts)
                            if (p == null) throw new IllegalArgumentException();

                        double k1 = point1.slopeTo(point2);
                        double k2 = point1.slopeTo(point3);
                        double k3 = point1.slopeTo(point4);
                        if (k1 == k2 && k1 == k3) {
                            // just find the 'min' and the max
                            Point min = point1;
                            Point max = point1;

                            for (int t = 1; t < 4; t++) {
                                if (min.compareTo(pts[t]) > 0)
                                    min = pts[t];
                                if (max.compareTo(pts[t]) < 0)
                                    max = pts[t];
                            }

                            segments.add(new LineSegment(min, max));
                        }
                    }
                }
            }
        }
    }

    // finds all line segments containing 4 or more points
    public int numberOfSegments() {
        return segments.size();
    }

    // the number of line segments
    public LineSegment[] segments() {
        return segments.toArray(LineSegment[]::new);
    }

    public static void main(String[] args) {
        Point p = new Point(8, 1);
        Point q = new Point(8, 4);
        Point r = new Point(8, 2);

        System.out.println(p.slopeOrder().compare(q, r));

    }
}

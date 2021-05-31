import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A faster, sorting-based solution. Remarkably, it is possible to solve the
 * problem much faster than the brute-force solution described above. Given
 * a point p, the following method determines whether p participates in a set
 * of 4 or more collinear points.
 * <p>
 * - Think of p as the origin.
 * - For each other point q, determine the slope it makes with p.
 * - Sort the points according to the slopes they makes with p.
 * - Check if any 3 (or more) adjacent points in the sorted order have equal
 * slopes with respect to p. If so, these points, together with p, are collinear.
 * <p>
 * - 将p视为原点。
 * - 对于每个其他点q，确定它与p形成的斜率。
 * - 根据点与p的斜率对点进行排序。
 * - 检查排序顺序中是否有3个（或更多）相邻点相等相对于p的斜率。如果是这样，则这些点与p一起共线。
 *
 * @author jtchen
 * @version 1.0
 * @date 2021/5/16 16:46
 */
public class FastCollinearPoints {
    private final List<LineSegment> segments;

    // the line segments
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        segments = new ArrayList<>();
        Point[] cPoints = new Point[points.length];
        System.arraycopy(points, 0, cPoints, 0, points.length);
        Point[] tmpPoints = new Point[points.length - 1];
        Arrays.sort(cPoints);

        for (int i = 0; i < cPoints.length; i++) {
            if (cPoints[i] == null) throw new IllegalArgumentException();
            Point basePoint = cPoints[i];

            // copy
            int tmpIdx = 0;
            for (int j = 0; j < cPoints.length; j++) {
                if (!cPoints[j].equals(basePoint))
                    tmpPoints[tmpIdx++] = cPoints[j];
            }

            // takes O(nlogn)
            Arrays.sort(tmpPoints, basePoint.slopeOrder());

            // find how many tmpPoints in the line
            int count = 2;
            for (int k = 1; k < tmpPoints.length; k++) {
                double k1 = tmpPoints[k - 1].slopeTo(basePoint);
                double k2 = tmpPoints[k].slopeTo(basePoint);

                if (k1 == k2) {
                    count++;
                    // 如果4点及以上共线,
                    // 并且otherPoints中与参考点共线且排在最左边的点比参考点大的话,
                    // 注意此处是遍历到头,所以索引是k-count+2
                    if (k == tmpPoints.length - 1) {
                        if (count >= 4 && basePoint.compareTo(tmpPoints[k - count + 2]) < 0) {
                            Point end = tmpPoints[k];
                            segments.add(new LineSegment(basePoint, end));
                        }
                    }
                }
                else {
                    if (count >= 4 && basePoint.compareTo(tmpPoints[k - count + 1]) < 0) {
                        Point end = tmpPoints[k - 1];
                        segments.add(new LineSegment(basePoint, end));
                    }
                    count = 2;
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
}

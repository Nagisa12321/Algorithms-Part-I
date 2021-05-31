/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     * <p>
     * 返回此点和指定点之间的斜率。
     * 形式上，如果两个点分别是（x0，y0）和（x1，y1），则斜率
     * 是（y1-y0）/（x1-x0）。为了完整起见，将斜率定义为
     * +0.0，如果连接两点的线段是水平的；
     * Double.POSITIVE_INFINITY（如果线段是垂直的）；否则为Double.POSITIVE_INFINITY。
     * 如果（x0，y0）和（x1，y1）相等，则返回Double.NEGATIVE_INFINITY。
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        else if (this.x == that.x) return Double.POSITIVE_INFINITY;
        return (double) (this.y - that.y) / (double) (this.x - that.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     * <p>
     * 通过y坐标比较两个点，通过x坐标断开关系。形式上，
     * 当且仅当y0 <y1或y0 = y1且x0 <x1时，调用点（x0，y0）小于自变量点（x1，y1）。
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        // maybe wrong :(
        if (this.y != that.y) {
            return this.y - that.y;
        }
        else {
            return this.x - that.x;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     * <p>
     * 比较两个点的斜率与该点的斜率。该斜率的定义与lopeTo（）方法相同。
     * <p>
     * The slopeOrder() method should return a comparator that compares
     * its two argument points by the slopes they make with the invoking
     * point (x0, y0). Formally, the point (x1, y1) is less than the point
     * (x2, y2) if and only if the slope (y1 − y0) / (x1 − x0) is
     * less than the slope (y2 − y0) / (x2 − x0). Treat horizontal,
     * vertical, and degenerate line segments as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return (point1, point2) -> {
            double k1 = Point.this.slopeTo(point1);
            double k2 = Point.this.slopeTo(point2);

            return Double.compare(k1, k2);
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

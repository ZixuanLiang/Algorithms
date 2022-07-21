import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class FastCollinearPoints {
    private List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        BruteCollinearPoints.ArgumentCheck(points);
        int len = points.length;
        for (int i = 0; i < len; i++) {
            Point p = points[i];
            Comparator<Point> comparator = p.slopeOrder();
            Arrays.sort(points, i + 1, len - 1, comparator);
            int counts = 1;
            double originalSlope = points[i + 1].slopeTo(p);
            for (int j = i + 2; j < len; j++) {
                double slope = points[j].slopeTo(p);
                if (slope == originalSlope) {
                    counts++;
                } else {
                    if (counts >= 3) {
                        segments.add(new LineSegment(p, points[j - 1]));
                    }
                    counts = 1;
                    originalSlope = slope;
                }
            }
        }
    }     // finds all line segments containing 4 or more points
    public           int numberOfSegments() {
        return segments.size();
    }        // the number of line segments

    public LineSegment[] segments() {
        return (LineSegment[]) segments.toArray();
    }                // the line segments

    public static void main(String[] args) {

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
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        Point[] pointsArray = ArgumentCheck(points);
        segments = new ArrayList<>();
        int len = pointsArray.length;
        for (int i = 0; i < len - 3; i++) {
            for (int j = i + 1; j < len - 2; j++) {
                for (int k = j + 1; k < len - 1; k++) {
                    for (int l = k + 1; l < len; l++) {
                        Point p = pointsArray[i];
                        Point q = pointsArray[j];
                        Point r = pointsArray[k];
                        Point s = pointsArray[l];
                        Comparator<Point> comparator = p.slopeOrder();
                        if (comparator.compare(q, r) == 0) {
                            if (comparator.compare(q, s)== 0) {
                                LineSegment lineSegment = new LineSegment(p, s);
                                segments.add(lineSegment);
                            }
                        }
                    }
                }
            }
        }
    }     // finds all line segments containing 4 points

    private static Point[] ArgumentCheck(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        } else {
            for (Point p : points) {
                if (p == null) {
                    throw new IllegalArgumentException();
                }
            }
        }
        Point[] pointArray = new Point[points.length];
        System.arraycopy(points, 0, pointArray, 0, points.length);
        Merge.sort(pointArray);
        for (int i = 0; i < pointArray.length - 1; i++) {
            if (pointArray[i].compareTo(pointArray[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        return pointArray;
    }
    public           int numberOfSegments() {
        return segments.size();
    }         // the number of line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
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

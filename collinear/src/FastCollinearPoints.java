import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        Point[] pointsArray = ArgumentCheck(points);
        segments = new ArrayList<>();
        int len = pointsArray.length;
        Point[] auxArray;
        for (Point p : pointsArray) {
            auxArray = Arrays.copyOf(pointsArray, len);
            Arrays.sort(auxArray);
            Comparator<Point> comparator = p.slopeOrder();
            Arrays.sort(auxArray, comparator);
            int counts = 1;
            double slopeOfLast = auxArray[0].slopeTo(p);
            for (int j = 1; j < len; j++) {
                double slope = auxArray[j].slopeTo(p);
                if (slope == slopeOfLast) {
                    counts++;
                    if (counts > 2 && j == len - 1) {
                        Point low = getLowPoint(p, auxArray[j - counts + 1]);
                        if (low == p) {
                            segments.add(new LineSegment(p, auxArray[j]));
                        }
                    }
                } else {
                    if (counts > 2) {
                        Point low = getLowPoint(p, auxArray[j - counts]);
                        if (low == p) {
                            segments.add(new LineSegment(p, auxArray[j - 1]));
                        }
                    }
                    counts = 1;
                    slopeOfLast = slope;
                }
            }
        }
    }
         // finds all line segments containing 4 or more points

    private Point getLowPoint(Point p, Point q) {
        if (p.compareTo(q) > 0) {
            return q;
        } else {
            return p;
        }
    }


    public int numberOfSegments() {
        return segments.size();
    }        // the number of line segments

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
}
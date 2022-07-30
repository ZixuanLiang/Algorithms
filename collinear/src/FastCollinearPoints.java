import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        Point[] pointsArray = ArgumentCheck(points);
        List<Point> list = Arrays.asList(pointsArray);
        LinkedList<Point> auxPointsArray = new LinkedList<>(list);
        segments = new ArrayList<>();
        int len = pointsArray.length;
        int i = 0;
        while (auxPointsArray.size() != 0 && i < len - 3){
            Point p = auxPointsArray.remove(0);
            Arrays.sort(pointsArray, i, len);
            int index = Arrays.binarySearch(pointsArray, p);
            Point temp = pointsArray[i];
            pointsArray[i] = pointsArray[index];
            pointsArray[index] = temp;

            Comparator<Point> comparator = p.slopeOrder();
            Arrays.sort(pointsArray, i + 1, len);
            Arrays.sort(pointsArray, i + 1, len, comparator);

            int counts = 1;
            double originalSlope = pointsArray[i + 1].slopeTo(p);

            for (int j = i + 2; j < len; j++) {
                double slope = pointsArray[j].slopeTo(p);
                if (slope == originalSlope) {
                    counts++;
                    if (j == len - 1 & counts >= 3) {
                        for (int k = counts; k > 0; k--) {
                            auxPointsArray.remove(pointsArray[j - k + 1]);
                        }
                        Point low = getLowPoint(p, pointsArray[j - counts + 1]);
                        Point high = getHighPoint(p, pointsArray[j]);
                        segments.add(new LineSegment(low, high));
                    }
                } else {
                    if (counts >= 3) {
                        for (int k = counts; k > 0; k--) {
                            auxPointsArray.remove(pointsArray[j - k]);
                        }
                        Point low = getLowPoint(p, pointsArray[j - counts]);
                        Point high = getHighPoint(p, pointsArray[j - 1]);
                        segments.add(new LineSegment(low, high));
                    }
                    counts = 1;
                    originalSlope = slope;
                }
            }
            i++;
        }
    }     // finds all line segments containing 4 or more points

    private Point getLowPoint(Point p, Point q) {
        if (p.compareTo(q) > 0) {
            return q;
        } else {
            return p;
        }
    }

    private Point getHighPoint(Point p, Point q) {
        if (p.compareTo(q) > 0) {
            return p;
        } else {
            return q;
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
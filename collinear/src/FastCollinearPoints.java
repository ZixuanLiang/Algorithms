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
        List<Double> slopeArray = new ArrayList<>();
        List<Point> pArray = new ArrayList<>();;
        segments = new ArrayList<>();
        int len = pointsArray.length;
        int i = 0;
        while (i < len - 3){
            Point p = pointsArray[i];
            Comparator<Point> comparator = p.slopeOrder();
            Arrays.sort(pointsArray, i + 1, len);
            Arrays.sort(pointsArray, i + 1, len, comparator);
            int counts = 1;
            double originalSlope = pointsArray[i + 1].slopeTo(p);
            for (int j = i + 2; j < len; j++) {
                double slope = pointsArray[j].slopeTo(p);
                if (slope != originalSlope) {
                    if (counts >= 3) {
                        Point low = getLowPoint(p, pointsArray[j - counts]);
                        Point high = getHighPoint(p, pointsArray[j - 1]);
                        boolean addOrNot = true;
                        for (int k = 0; k < slopeArray.size(); k++) {
                            if (slopeArray.get(k) == originalSlope) {
                                if (sameLine(pArray.get(k), slopeArray.get(k), low)) {
                                    addOrNot = false;
                                    break;
                                }
                            }
                        }
                        if (addOrNot) {
                            segments.add(new LineSegment(low, high));
                            slopeArray.add(originalSlope);
                            pArray.add(low);
                        }
                    }
                    counts = 1;
                    originalSlope = slope;
                } else {
                    counts++;
                    if (j == len - 1 && counts >= 3){
                        Point low = getLowPoint(p, pointsArray[j - counts + 1]);
                        Point high = getHighPoint(p, pointsArray[j]);
                        boolean addOrNot = true;
                        for (int k = 0; k < slopeArray.size(); k++) {
                            if (slopeArray.get(k) == originalSlope) {
                                if (sameLine(pArray.get(k), slopeArray.get(k), low)) {
                                    addOrNot = false;
                                    break;
                                }
                            }
                        }
                        if (addOrNot) {
                            segments.add(new LineSegment(low, high));
                            slopeArray.add(originalSlope);
                            pArray.add(low);
                        }
                    }
                }
            }
            i++;
        }
    }

        // finds all line segments containing 4 or more points

    private boolean sameLine(Point o, Double slope,Point p){
        return p.slopeTo(o) == slope;
    }
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
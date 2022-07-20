import edu.princeton.cs.algs4.Merge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        ArgumentCheck(points);
        segments = new ArrayList<>();
        int len = points.length;
        for (int i = 0; i < len - 3; i++) {
            for (int j = i + 1; j < len - 2; i++) {
                for (int k = j + 1; k < len - 1; k++) {
                    for (int l = k + 1; l < len; l++) {
                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[l];
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

    private void ArgumentCheck(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        } else {
            for (Point p : points) {
                if (p == null) {
                    throw new IllegalArgumentException();
                }
            }
        }
        Merge.sort(points);
        for (int i = 0; i < points.length; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }
    public           int numberOfSegments() {
        return segments.size();
    }         // the number of line segments
    public LineSegment[] segments() {
        return (LineSegment[]) segments.toArray();
    }                // the line segments
}
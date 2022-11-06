import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        argumentCheck(p);
        pointSet.add(p);
    }

    private void argumentCheck(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException();
        }
    }

    public boolean contains(Point2D p) {
        argumentCheck(p);
        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        argumentCheck(rect);
        ArrayList<Point2D> pointList = new ArrayList<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                pointList.add(p);
            }
        }
        return pointList;
    }

    public Point2D nearest(Point2D p){
        argumentCheck(p);
        Point2D nearestPoint = pointSet.first();
        double minDistance = nearestPoint.distanceTo(p);
        for (Point2D point : pointSet) {
            if (point.distanceTo(p) < minDistance) {
                nearestPoint = point;
                minDistance = point.distanceTo(p);
            }
        }
        return nearestPoint;
    }
}


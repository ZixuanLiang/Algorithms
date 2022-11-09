import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private class Node {
        private Point2D point;
        private int indicator;
        private Node leftChild, rightChild = null;
        private RectHV area;

        public Node(Point2D p, int indicator, RectHV area) {
            this.point = p;
            this.indicator = indicator;
            this.area = area;
        }
    }

    private Node root;
    private int size;

    public KdTree() {
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        insertToTree(root, p);
    }
    private void insertToTree (Node root, Point2D p) {
        argumentCheck(p);
        if (size() == 0) {
            this.root = new Node(p, 1, new RectHV(0, 0, 1, 1));
            size++;
            return;
        }
        double leftOrRight = leftOrRight(root, p);
        // base case
        if (root.leftChild == null && leftOrRight <= 0 && !root.point.equals(p)) {
            if (root.indicator > 0) {//the area is left part of the parent area
                root.leftChild = new Node(p, root.indicator * (-1), new RectHV(root.area.xmin(), root.area.ymin(),root.point.x(), root.area.ymax()));
            } else {
                root.leftChild = new Node(p, root.indicator * (-1), new RectHV(root.area.xmin(), root.area.ymin(),root.area.xmax(),root.point.y()));
            }
            size++;
            return;
        } else if (root.rightChild == null && leftOrRight > 0 && !root.point.equals(p)){
            if (root.indicator > 0) {//the area is right part of the parent area
                root.rightChild = new Node(p, root.indicator * (-1), new RectHV(root.point.x(), root.area.ymin(),root.area.xmax(), root.area.ymax()));
            } else {
                root.rightChild = new Node(p, root.indicator * (-1), new RectHV(root.area.xmin(), root.point.y(), root.area.xmax(),root.area.ymax()));
            }
            size++;
            return;
        }
        // recursive case
        if (leftOrRight > 0) {
            insertToTree(root.rightChild, p);
        } else {
            insertToTree(root.leftChild, p);
        }
    }
    // a helper function helps determine which child node needs to be examined
    // return a positive value if rightChild needed to be considered; non-positive means leftChild needs to be considered
    private double leftOrRight(Node root, Point2D p) {
        double leftOrRight;
        if (root.indicator == 1) {
            leftOrRight = p.x() - root.point.x();
        } else {
            leftOrRight = p.y() - root.point.y();
        }
        return leftOrRight;
    }

    public boolean contains(Point2D p) {
        return contains(this.root, p);
    }
    private boolean contains(Node root, Point2D p) {
        argumentCheck(p);
        if (root == null ) {
            return false;
        } else if (root.point.equals(p)) {
            return true;
        } else {
            double leftOrRight = leftOrRight(root, p);
            if (leftOrRight > 0) {
                return contains(root.rightChild, p);
            } else {
                return contains(root.leftChild, p);
            }
        }
    }

    private void argumentCheck(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException();
        }
    }
    public void draw() {
        draw(root);
    }
    private void draw(Node root) {
        if (root == null) {
            return;
        }
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(Color.black);
        StdDraw.point(root.point.x(), root.point.y());
        StdDraw.setPenRadius(0.01);
        if (root.indicator > 0) {
            StdDraw.setPenColor(Color.red);
            StdDraw.line(root.point.x(), root.area.ymin(), root.point.x(), root.area.ymax());
        } else {
            StdDraw.setPenColor(Color.blue);
            StdDraw.line(root.area.xmin(), root.point.y(), root.area.xmax(), root.point.y());
        }
        draw(root.leftChild);
        draw(root.rightChild);
    }

    public Iterable<Point2D> range(RectHV rect) {
        argumentCheck(rect);
        if (root == null) {
            return null;
        }
        List<Point2D> list = new ArrayList<>();
        addAllPointsInATree(rect, root, list);
        return list;
    }

    private void addAllPointsInATree (RectHV rect, Node root, List<Point2D> list) {
        if (rect.contains(root.point)) {
            list.add(root.point);
        }
        if (root.leftChild != null && rect.intersects(root.leftChild.area)) {
            addAllPointsInATree(rect, root.leftChild, list);
        }
        if (root.rightChild != null && rect.intersects(root.rightChild.area)) {
            addAllPointsInATree(rect, root.rightChild, list);
        }
    }

    public Point2D nearest(Point2D p) {
        argumentCheck(p);
        if (size() == 0) {
            return null;
        } else {
            return nearestPoint(root, p,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  root.point);
        }
    }

    private Point2D nearestPoint(Node root, Point2D p, Point2D nearestPointSoFar) {
        if (root.point.distanceTo(p) < nearestPointSoFar.distanceTo(p)) {
            nearestPointSoFar = root.point;
        }
        if (leftOrRight(root, p) <= 0) {
            if (root.leftChild != null && root.leftChild.area.distanceTo(p) < nearestPointSoFar.distanceTo(p)) {
                nearestPointSoFar = nearestPoint(root.leftChild, p, nearestPointSoFar);
            }
            if (root.rightChild != null && root.rightChild.area.distanceTo(p) < nearestPointSoFar.distanceTo(p)) {
                nearestPointSoFar = nearestPoint(root.rightChild, p, nearestPointSoFar);
            }
        } else {
            if (root.rightChild != null && root.rightChild.area.distanceTo(p) < nearestPointSoFar.distanceTo(p)) {
                nearestPointSoFar = nearestPoint(root.rightChild, p, nearestPointSoFar);
            }
            if (root.leftChild != null && root.leftChild.area.distanceTo(p) < nearestPointSoFar.distanceTo(p)) {
                nearestPointSoFar = nearestPoint(root.leftChild, p, nearestPointSoFar);
            }
        }
        return nearestPointSoFar;
    }
}

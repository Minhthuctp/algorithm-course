import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private static class Node {
        private final Point2D p;
        private final int direction; // 0 - horizontal, 1 - vertical
        private Node pLeft;
        private Node pRight;
        private final RectHV rect;

        Node(Point2D _p, int _direction, Node _pLeft, Node _pRight, RectHV _rect) {
            p = _p;
            direction = _direction;
            pLeft = _pLeft;
            pRight = _pRight;
            if (_rect==null)
                rect = new RectHV(0,0,1,1);
            else
                rect = _rect;
        }

        public int compareTo(Point2D other) {
            if (direction == 0) {
                return Double.compare(this.p.y(), other.y());
            } else {
                return Double.compare(this.p.x(), other.x());
            }
        }

    }

    private Node root;
    private int size;

    public KdTree() // construct an empty set of points
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty() // is the set empty?
    {
        return (root == null);
    }

    public int size() // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
        {
            root = new Node(p, 1, null, null,null);
            size++;
        }
        else
        {
            if (!contains(p)) {
                insert(root, p, root.rect);
                size++;
            }
        }
    }

    private void insert(Node root, Point2D p, RectHV rect) {
        if (root.compareTo(p) > 0) {
            if (root.pLeft==null)
            {
                if (root.direction==1)
                    root.pLeft = new Node(p, (root.direction + 1) % 2, null, null,new RectHV(rect.xmin(),rect.ymin(),root.p.x(),rect.ymax()));
                else
                    root.pLeft = new Node(p, (root.direction + 1) % 2, null, null,new RectHV(rect.xmin(),rect.ymin(),rect.xmax(),root.p.y()));
            }
            else
                insert(root.pLeft, p ,root.pLeft.rect);
        }
        else
        {
            if (root.pRight == null) {
                if (root.direction == 1)
                    root.pRight = new Node(p, (root.direction + 1) % 2, null, null, new RectHV(root.p.x(), rect.ymin(), rect.xmax(), rect.ymax()));
                else
                    root.pRight = new Node(p, (root.direction + 1) % 2, null, null, new RectHV(rect.xmin(), root.p.y(), rect.xmax(), rect.ymax()));
            } else
                insert(root.pRight, p, root.pRight.rect);
        }
    }

    private boolean contains(Node root, Point2D p) {

        if (root == null)
            return false;
        if (root.p.equals(p))
            return true;
        if (root.compareTo(p) > 0) {
            return contains(root.pLeft, p);
        } else {
            return contains(root.pRight, p);
        }
    }

    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null) {
            return false;
        } else {
            return contains(root, p);
        }
    }

    private void draw(Node root, RectHV rectHV){
        if (root == null){
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        root.p.draw();

        StdDraw.setPenRadius(0.001);
        if (root.direction == 1){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(root.p.x(), rectHV.ymin(), root.p.x(), rectHV.ymax());
            draw(root.pLeft, new RectHV(rectHV.xmin(), rectHV.ymin(), root.p.x(), rectHV.ymax()));
            draw(root.pRight, new RectHV(root.p.x(), rectHV.ymin(), rectHV.xmax(), rectHV.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rectHV.xmin(), root.p.y(), rectHV.xmax(), root.p.y());
            draw(root.pLeft, new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), root.p.y()));
            draw(root.pRight, new RectHV(rectHV.xmin(), root.p.y(), rectHV.xmax(), rectHV.ymax()));
        }
    }

    public void draw() // draw all points to standard draw
    {
        draw(root, new RectHV(0,0, 1, 1));
    }
    private void range(Node root, RectHV rect, List<Point2D> res)
    {
        if (root == null)
            return;
        if (rect.contains(root.p))
            res.add(root.p);
        if (root.direction == 1)
        {
            if (rect.xmin() <= root.p.x() && root.p.x() <= rect.xmax())
            {
                range(root.pLeft, rect, res);
                range(root.pRight, rect, res);
            }
            else
            {
                if (rect.xmin() > root.p.x())
                {
                    range(root.pRight, rect, res);
                }
                else
                {
                    range(root.pLeft, rect, res);
                }
            }
        }
        else
        {
            if (rect.ymin() <= root.p.y() && root.p.y() <= rect.ymax())
            {
                range(root.pLeft, rect, res);
                range(root.pRight, rect, res);
            }
            else
            {
                if (rect.ymin() > root.p.y())
                {
                    range(root.pRight, rect, res);
                }
                else
                {
                    range(root.pLeft, rect, res);
                }
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> res = new LinkedList<>();
        range(this.root, rect, res);
        return res;
    }

    private Point2D nearest( Point2D p, Point2D cur, Node root)
    {
        Point2D minPoint = cur;

        if (root == null)
            return minPoint;
        if (p.distanceSquaredTo(root.p) < p.distanceSquaredTo(minPoint))
            minPoint = root.p;
        if (root.direction == 1)
        {
            if (root.p.x() < p.x())
            {
                minPoint = nearest(p, minPoint, root.pRight);
                if (root.pLeft != null  && (minPoint.distanceSquaredTo(p) > root.pLeft.rect.distanceSquaredTo(p)))
                    minPoint = nearest(p, minPoint, root.pLeft);
            }
            else
            {
                minPoint = nearest(p, minPoint, root.pLeft);
                if (root.pRight != null && (minPoint.distanceSquaredTo(p) > root.pRight.rect.distanceSquaredTo(p)))
                    minPoint = nearest( p, minPoint, root.pRight);
            }
        }
        else {
            if (root.p.y() < p.y()) {
                minPoint = nearest(p, minPoint, root.pRight);
                if (root.pLeft != null  && (minPoint.distanceSquaredTo(p) > root.pLeft.rect.distanceSquaredTo(p)))
                    minPoint = nearest(p, minPoint, root.pLeft);
            }
            else
            {
                minPoint = nearest(p, minPoint, root.pLeft);
                if (root.pRight != null && (minPoint.distanceSquaredTo(p) > root.pRight.rect.distanceSquaredTo(p)))
                    minPoint = nearest( p, minPoint, root.pRight);
            }
        }
        return minPoint;
    }
    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
            return null;
        return nearest(p,root.p,root);
    }


    public static void main(String[] args) // unit testing of the methods (optional)
    {
        KdTree kdTree = new KdTree();

        kdTree.insert(new Point2D(0.7, 0.2)); // A
        kdTree.insert(new Point2D(0.5, 0.4)); // B
        kdTree.insert(new Point2D(0.2, 0.3)); // C
        kdTree.insert(new Point2D(0.4, 0.7)); // D
        kdTree.insert(new Point2D(0.9, 0.6)); // E


        //System.out.println(kdTree.nearest(new Point2D(0.078, 0.552)));
        System.out.println(kdTree.nearest(new Point2D(0.7, 0.903)));
    }
}

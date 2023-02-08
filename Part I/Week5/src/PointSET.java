import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

import java.util.TreeSet;


public class PointSET {
    private TreeSet<Point2D> t_set;
    public PointSET() // construct an empty set of points
    {
        t_set = new TreeSet<>();
    }
    public boolean isEmpty() // is the set empty?
    {
        return t_set.isEmpty();
    }
    public int size() // number of points in the set
    {
        return t_set.size();
    }
    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (contains(p)==false)
        {
            t_set.add(p);
        }
    }
    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException();
        return t_set.contains(p);
    }
    public void draw() // draw all points to standard draw
    {

        for (Point2D p: t_set)
        {
            p.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
            throw new IllegalArgumentException();
        TreeSet<Point2D> res = new TreeSet<>();
        for (Point2D p: t_set)
            if (rect.contains(p))
                res.add(p);
        return res;
    }
    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p==null)
            throw new IllegalArgumentException();
        double nearestDist = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D point : t_set) {
            double dist = point.distanceSquaredTo(p);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
    public static void main(String[] args) // unit testing of the methods (optional)
    {
        PointSET pq = new PointSET();
        pq.insert(new Point2D(0.1,0.2));
        pq.insert(new Point2D(0.3,0.2));
        pq.insert(new Point2D(0.2,0.4));

        pq.draw();
    }
}
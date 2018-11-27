package edu.gsu.stability.algorithm;

import edu.gsu.stability.model.Graph;
import edu.gsu.stability.util.MM;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChakravartiWagelmans {

    /**
     * Finds radius of stability of given grapg g
     *
     * @param g input graph
     * @param c matrix of costs in objective-function for each edge
     * @return radius of stability for given graph
     */
    public double radius(Graph g, double[][] c) {
        Kruskal kruskal = new Kruskal();
        kruskal.log = false;
        Graph gC = g.copy();
        gC.m = c;
        Graph.Edge[] mst = kruskal.kruskalMST(gC);
        //optimal solusion of P(c)
        int[][] x0 = edgesToBoolMatrix(mst, gC.n);
        int[][] d = new int[gC.n][gC.n];
        for (int i = 0; i < gC.n; i++) {
            for (int j = i + 1; j < gC.n; j++) {

                d[i][j] = 1 - 2 * x0[i][j];
            }
        }
        // calculate v0(r)
        double cNormInf = MM.max(c);
        Graph.Edge[] secondBest = kruskal.kruskalMSTSecondBest(gC);
        int[][] x_ = edgesToBoolMatrix(secondBest, gC.n);
        SegmentFunction v0 = vR(x_, c, d, cNormInf);

        // find solution for c_, where c_ <- c- ||c||d line 5
        double[][] c_ = MM.diff(c, MM.mul(d, cNormInf));
        gC.m = c_;
        // line 6
        mst = kruskal.kruskalMST(gC);
        x_ = edgesToBoolMatrix(mst, gC.n);
        if (MM.eq(x_, x0)) {
            mst = kruskal.kruskalMSTSecondBest(gC);
            x_ = edgesToBoolMatrix(mst, gC.n);
        }
        // line 7
        SegmentFunction v_c = vR(x_, c, d, cNormInf);
        List<Point> inter = v0.intersection(v_c);
        if (inter.size() != 1) {
            System.out.println("Intersection of two segments is not a point!");
            return -1;
        }
        SegmentFunction v = new SegmentFunction(v0.points);
        // hack to merge two initial function
        v.points.remove(1);
        v.points.add(v_c.points.get(1));
        // end hack
        v.addPoint(inter.get(0));
        List<Point> I = new ArrayList<>(inter);
        while (!I.isEmpty()) {
            Point p_ = I.get(0);
            I.remove(0);
            c_ = MM.diff(c, MM.mul(d, p_.x));
            gC.m = c_;
            mst = kruskal.kruskalMST(gC);
            x_ = edgesToBoolMatrix(mst, gC.n);
            if (MM.eq(x_, x0)) {
                mst = kruskal.kruskalMSTSecondBest(gC);
                x_ = edgesToBoolMatrix(mst, gC.n);
            }
            double solution = MM.dotProd(c_, x_);
            if (solution < p_.y) {
                SegmentFunction vp_ = vR(x_, c, d, cNormInf);
                inter = v.intersection(vp_);
                if (inter.size() == 1) {
                    System.out.println("Just one point in intersection");
                    continue;
                }
                if (inter.size() != 2) {
                    System.out.println("Intersection of function and segment is not two points!");
                    return -1;
                }
                v.addTwoPoints(inter.get(0), inter.get(1));
                I.add(inter.get(0));
                I.add(inter.get(1));
            }
        }
        SegmentFunction vr0 = vR0(x0, c, MM.norm1(x0), cNormInf);
        inter = v.intersection(vr0);
        if (inter.size() != 1) {
            System.out.println("Not one point in v and vx0 intersection!");
            return -1;
        }
        return inter.get(0).x;
    }

    /**
     * Calculates v(r) function based on given solution x, objective function coefficients c
     *
     * @param x        given solution of ILP
     * @param c        given objective function coefficients
     * @param d        special matrix d
     * @param cNormInf norm of a vector c
     * @return Function from 0 to cNormInf v(r) <- (c-rd)x
     */
    private SegmentFunction vR(int[][] x, double[][] c, int[][] d, double cNormInf) {
        double k = -MM.dotProd(d, x);
        double b = MM.dotProd(c, x);
        List<Point> points = new LinkedList<>();
        points.add(new Point(0, b));
        points.add(new Point(cNormInf, cNormInf * k + b));
        return new SegmentFunction(points);
    }

    private SegmentFunction vR0(int[][] x, double[][] c, double xNorm, double cNorm) {
        double k = xNorm;
        double b = MM.dotProd(c, x);
        List<Point> points = new LinkedList<>();
        points.add(new Point(0, b));
        points.add(new Point(cNorm, cNorm * k + b));
        return new SegmentFunction(points);
    }

    private int[][] edgesToBoolMatrix(Graph.Edge[] edges, int n) {
        int[][] x0 = new int[n][n];
        // consider only upper right matrix
        for (int i = 0; i < edges.length; i++) {
            if (edges[i].src < edges[i].dest) {
                x0[edges[i].src][edges[i].dest] = 1;
            } else {
                x0[edges[i].dest][edges[i].src] = 1;
            }
        }
        return x0;
    }

    private class SegmentFunction {
        public List<Point> points;

        public SegmentFunction() {
        }

        public SegmentFunction(List<Point> points) {
            this.points = points;
        }

        /**
         * Finds intersection of two segment functions
         *
         * @param trg second function
         * @return List of intersection points sorted from smaller x to bigger
         */
        public List<Point> intersection(SegmentFunction trg) {
            List<Point> result = new ArrayList<>();

            for (int i = 0; i < trg.points.size() - 1; i++) {
                for (int j = 0; j < points.size() - 1; j++) {
                    Point point = segmentsIntersection(trg.points.get(i), trg.points.get(i + 1), points.get(j), points.get(j + 1));
                    if (point.x < Double.MAX_VALUE) {
                        result.add(point);
                    }
                }
            }
            result.sort(Comparator.comparing(p -> p.x));
            result = result.stream().filter(distinctByKey(p -> p.x)).collect(Collectors.toList());
            return result;
        }

        private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
            Set<Object> seen = ConcurrentHashMap.newKeySet();
            return t -> seen.add(keyExtractor.apply(t));
        }

        private Point segmentsIntersection(Point A, Point B, Point C, Point D) {
            // Line AB represented as a1x + b1y = c1
            double a1 = B.y - A.y;
            double b1 = A.x - B.x;
            double c1 = a1 * (A.x) + b1 * (A.y);

            // Line CD represented as a2x + b2y = c2
            double a2 = D.y - C.y;
            double b2 = C.x - D.x;
            double c2 = a2 * (C.x) + b2 * (C.y);

            double determinant = a1 * b2 - a2 * b1;

            if (determinant == 0) {
                // The lines are parallel. This is simplified
                // by returning a pair of FLT_MAX
                return new Point(Double.MAX_VALUE, Double.MAX_VALUE);
            } else {
                double x = (b2 * c1 - b1 * c2) / determinant;
                double y = (a1 * c2 - a2 * c1) / determinant;
                double fmin = Math.min(A.x, B.x);
                double fmax = Math.max(A.x, B.x);
                double smin = Math.min(C.x, D.x);
                double smax = Math.max(C.x, D.x);
                if (x >= fmin && x >= smin && x <= fmax && x <= smax) {
                    return new Point(x, y);
                } else {
                    // lines intersect but not segments
                    return new Point(Double.MAX_VALUE, Double.MAX_VALUE);
                }
            }
        }

        public void addPoint(Point a) {
            for (int i = 0; i < this.points.size() - 1; i++) {
                if (this.points.get(i).x < a.x && this.points.get(i + 1).x > a.x) {
                    this.points.add(i + 1, a);
                    break;
                }
            }
        }

        /**
         * Adds two points to function and removes everything in between them
         *
         * @param a first point
         * @param b second point
         */
        public void addTwoPoints(Point a, Point b) {
            addPoint(a);
            addPoint(b);
            this.points.removeIf(next -> next.x > a.x && next.x < b.x);
        }

    }

    private class Point {
        public double x, y;

        public Point() {
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}

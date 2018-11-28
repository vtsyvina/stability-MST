package edu.gsu.stability.algorithm;

import edu.gsu.stability.model.Graph;

public class Kuzmin {

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
        Graph.Edge[] mst = kruskal.mst(g);
        Graph.Edge[] second = kruskal.secondMst(g);
        return (kruskal.MSTWeight(second)-kruskal.MSTWeight(mst))/2;
    }
}

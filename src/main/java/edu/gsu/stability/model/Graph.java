package edu.gsu.stability.model;

import edu.gsu.stability.util.MM;

import java.util.LinkedList;
import java.util.List;

public class Graph {
    public int n;
    public double[][] m;

    // A class to represent a graph edge
    public static class Edge implements Comparable<Edge> {
        public int src, dest;
        public double weight;

        // Comparator function used for sorting edges
        // based on their weight
        public int compareTo(Edge compareEdge) {
            return this.weight > compareEdge.weight ? 1 : -1;
        }
    }

    public Edge[] getEdgeList(){
        List<Edge> le = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (m[i][j] != Double.MAX_VALUE) {
                    Graph.Edge e = new Graph.Edge();
                    e.src = i;
                    e.dest = j;
                    e.weight = m[i][j];
                    le.add(e);
                }
            }
        }
        Graph.Edge[] edges = new Graph.Edge[le.size()];
        edges = le.toArray(edges);
        return edges;
    }

    public Graph copy(){
        Graph copy = new Graph();
        copy.n = n;
        copy.m = MM.copy(m);
        return copy;
    }

    public void printm() {
    for (int row = 0; row < this.m.length; row++) {
        for (int col = 0; col < this.m[row].length; col++) {
            System.out.printf("%5.1f ", this.m[row][col]);
        }
        System.out.println();
    }
}

}

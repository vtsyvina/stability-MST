package edu.gsu.stability.algorithm;

import edu.gsu.stability.model.Graph;

import java.util.Arrays;

public class Kruskal {

    public boolean log = true;

    

    // A class to represent a subset for union-find
    class subset {
        int parent, rank;
    }

    ;

    // The main function to construct MST using Kruskal's algorithm
    public Graph.Edge[] kruskalMST(Graph g) {
        Graph.Edge[] edges = g.getEdgeList();
        int V = g.n;
        Graph.Edge result[] = new Graph.Edge[V - 1];  // Tnis will store the resultant MST
        int e = 0;  // An index variable, used for result[]
        int i = 0;  // An index variable, used for sorted edges
        for (i = 0; i < V - 1; ++i)
            result[i] = new Graph.Edge();

        // Step 1:  Sort all the edges in non-decreasing order of their
        // weight.  If we are not allowed to change the given graph, we
        // can create a copy of array of edges
        Arrays.sort(edges);

        // Allocate memory for creating V ssubsets
        subset subsets[] = new subset[V];
        for (i = 0; i < V; ++i)
            subsets[i] = new subset();

        // Create V subsets with single elements
        for (int v = 0; v < V; ++v) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0;  // Index used to pick next edge

        // Number of edges to be taken is equal to V-1
        while (e < V - 1) {
            // Step 2: Pick the smallest edge. And increment
            // the index for next iteration
            Graph.Edge next_edge = new Graph.Edge();
            next_edge = edges[i++];

            int x = find(subsets, next_edge.src);
            int y = find(subsets, next_edge.dest);

            // If including this edge does't cause cycle,
            // include it in result and increment the index
            // of result for next edge
            if (x != y) {
                result[e++] = next_edge;
                Union(subsets, x, y);
            }
            // Else discard the next_edge
        }

        // print the contents of result[] to display
        // the built MST
        if (log) {
            printEdges(result);
        }

        return result;
    }

    public Graph.Edge[] kruskalMSTSecondBest(Graph g) {
        Graph.Edge[] edges = kruskalMST(g);
        Graph copy = g.copy();
        Graph.Edge[] secondBest = new Graph.Edge[g.n - 1];
        double secondSum = 1_000_000_000;
        for (int i = 0; i < edges.length; i++) {
            int src = edges[i].src;
            int dest = edges[i].dest;
            copy.m[src][dest] = Double.MAX_VALUE;
            copy.m[dest][src] = Double.MAX_VALUE;
            Graph.Edge[] tmp = kruskalMST(copy);
            double sum = MSTWeight(tmp);
            if (secondSum > sum) {
                secondSum = sum;
                secondBest = tmp;
            }
            copy.m[src][dest] = g.m[src][dest];
            copy.m[dest][src] = g.m[dest][src];
        }
        return secondBest;
    }

    double MSTWeight(Graph.Edge[] edges) {
        double sum = 0;
        for (int i = 0; i < edges.length; i++) {
            sum += edges[i].weight;
        }
        return sum;
    }

    // A utility function to find set of an element i
    // (uses path compression technique)
    int find(subset subsets[], int i) {
        // find root and make root as parent of i (path compression)
        if (subsets[i].parent != i)
            subsets[i].parent = find(subsets, subsets[i].parent);

        return subsets[i].parent;
    }

    // A function that does union of two sets of x and y
    // (uses union by rank)
    void Union(subset subsets[], int x, int y) {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        // Attach smaller rank tree under root of high rank tree
        // (Union by Rank)
        if (subsets[xroot].rank < subsets[yroot].rank)
            subsets[xroot].parent = yroot;
        else if (subsets[xroot].rank > subsets[yroot].rank)
            subsets[yroot].parent = xroot;

            // If ranks are same, then make one as root and increment
            // its rank by one
        else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    public void printEdges(Graph.Edge[] edges) {
        System.out.println("Following are the edges in " +
                "the constructed MST");
        for (int i = 0; i < edges.length; ++i)
            System.out.println(edges[i].src + " -- " + edges[i].dest + " == " + edges[i].weight);
    }
}

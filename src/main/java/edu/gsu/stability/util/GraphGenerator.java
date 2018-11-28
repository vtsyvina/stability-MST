package edu.gsu.stability.util;

import edu.gsu.stability.model.Graph;

import java.util.Random;

public class GraphGenerator {


    public static Graph generate(int n, int max, double density, long seed){
        Graph graph = new Graph();
        graph.n = n;
        Random random = seed == 0 ? new Random() : new Random(seed);
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                if (random.nextDouble() < density){
                    m[i][j] = max*random.nextDouble();
                    m[j][i] = m[i][j];
                } else{
                    m[i][j] = Double.MAX_VALUE;
                    m[j][i] = m[i][j];
                }
            }
        }
        graph.m = m;
        return graph;
    }
}

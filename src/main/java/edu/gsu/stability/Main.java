package edu.gsu.stability;

import edu.gsu.stability.algorithm.ChakravartiWagelmans;
import edu.gsu.stability.model.Graph;
import edu.gsu.stability.util.MM;
import edu.gsu.stability.util.Reader;

public class Main {
    public static void main(String[] args) {
        Graph graph = Reader.readGraph(args[0]);

        ChakravartiWagelmans chakravartiWagelmans = new ChakravartiWagelmans();
        double[][] copy = MM.copy(graph.m);
        for (int i = 0; i < graph.n; i++) {
            for (int j = 0; j < i; j++) {
                copy[i][j] = 0;
            }
        }
        double radius = chakravartiWagelmans.radius(graph, copy);
        System.out.println("Radius = "+radius);
    }
}

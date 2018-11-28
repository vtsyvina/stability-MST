package edu.gsu.stability;

import edu.gsu.stability.algorithm.ChakravartiWagelmans;
import edu.gsu.stability.algorithm.Kuzmin;
import edu.gsu.stability.model.Graph;
import edu.gsu.stability.util.GraphGenerator;
import edu.gsu.stability.util.MM;
import edu.gsu.stability.util.Reader;

import java.util.Random;

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

    private static void testGenerator() {
        Graph graph;
        int n = 200;
        // good example
        //graph = GraphGenerator.generate(n, 10, 1, 1);
        int iter = 200;
        int c = 0;
        int error = 0;
        for (int i = 0; i < iter; i++) {
            graph = GraphGenerator.generate(n, 2000, 1, i);
            if (n < 20) {
                graph.printm();
            }
            ChakravartiWagelmans chakravartiWagelmans = new ChakravartiWagelmans();
            Kuzmin kuzmin = new Kuzmin();
            double[][] copy = MM.copy(graph.m);
            for (int j = 0; j < graph.n; j++) {
                for (int k = 0; k < k; k++) {
                    copy[i][k] = 0;
                }
            }
            double radius = chakravartiWagelmans.radius(graph, copy);
            double kus = kuzmin.radius(graph, copy);
            if (Math.abs(radius-kus) > 0.0001){
                System.out.println("Alarma!");
                c++;
            }
            if (Math.abs(radius +1) < 0.0001){
                error++;
            }
            System.out.println("Radius = " + radius);
            System.out.println("Kuzmin = " + kus);

        }
        System.out.println("c="+c);
        System.out.println("error="+error);
    }
}

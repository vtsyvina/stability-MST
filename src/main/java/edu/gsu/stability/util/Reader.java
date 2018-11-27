package edu.gsu.stability.util;

import edu.gsu.stability.model.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Reader {
    public static Graph readGraph(String filepath) {
        try {
            List<String> strings = Files.readAllLines(Paths.get(filepath));
            int n = Integer.valueOf(strings.get(0));
            double[][] m = new double[n][n];
            for (int i = 1; i < strings.size(); i++) {
                String[] s = strings.get(i).split(" ");
                for (int j = 0; j < n; j++) {
                    if (s[j].equals('X')){
                        m[i-1][j] = Double.MAX_VALUE;
                    } else {
                        m[i - 1][j] = Double.valueOf(s[j]);
                    }
                }
            }
            Graph g = new Graph();
            g.n = n;
            g.m = m;
            return g;
        } catch (IOException e) {
            System.out.println("Oh no!");
            e.printStackTrace();
        }
        return null;
    }
}

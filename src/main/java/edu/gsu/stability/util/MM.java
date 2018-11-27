package edu.gsu.stability.util;

/**
 * Class to perform different manipulations with matrices
 */
public class MM {

    public static double[][] copy(double[][] src){
        int n = src.length;
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++) {
            m[i] = src[i].clone();
        }
        return m;
    }

    public static double max(double[][] x){
        int n = x.length;
        double max = -1_000_000;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (x[i][j] > max){
                    max = x[i][j];
                }
            }
        }
        return max;
    }

    public static boolean eq(int[][] x, int[][] y){
        int n = x.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (x[i][j] != y[i][j])
                    return false;
            }
        }
        return true;
    }

    public static double[][] mul(int[][] x, double coef){
        int n = x.length;
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = x[i][j] * coef;
            }
        }
        return res;
    }


    public static double dotProd(double[][] x, int[][] y){
        int n = x.length;
        double s = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                s += x[j][i] * y[j][i];
            }
        }
        return s;
    }

    public static double dotProd(int[][] x, int[][] y){
        int n = x.length;
        double s = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                s += x[j][i] * y[j][i];
            }
        }
        return s;
    }

    public static double[][] diff(double[][] x, double[][] y){
        int n = x.length;
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = x[i][j] -y[i][j];
            }
        }
        return res;
    }

    public static int norm1(int[][] x){
        int n = x.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res += x[i][j];
            }
        }
        return res;
    }


}

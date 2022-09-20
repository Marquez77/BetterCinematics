package me.marquez.bettercinematics.utils;

public class MathUtils {

    public static double[][] matrixMultiply(double[][] a, double[][] b) {
        double[][] result = new double[a.length][b[0].length];

        for(int i = 0 ; i < a.length ; ++i){
            for(int j = 0 ; j < b[0].length ; ++j){
                for(int k = 0 ; k < a[0].length ; ++k) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return result;

    }

}

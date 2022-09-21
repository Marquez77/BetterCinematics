package me.marquez.bettercinematics.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SplineUtils {

    private static final double[][] quadraticMatrix = new double[][] {
            {2, -4, 2},
            {-3, 4, -1},
            {1, 0, 0}
    };

    private static final double[][] cubicMatrix = new double[][] {
            {-1, 3, -3, 1},
            {2, -5, 4, -1},
            {-1, 0, 1, 0},
            {0, 2, 0, 0}
    };

    public static BiFunction<Integer, Double, XY> getCubicSpline(List<XY> points) {
        List<Function<Double, XY>> functions = new ArrayList<>();
        if(points.size() > 2) {
            //Point 1 to Point 2 -> quadratic function
            double[][] beginMatrix = MathUtils.matrixMultiply(quadraticMatrix, makeMatrix(points.subList(0, 3)));
            functions.add(t -> new XY(executeQuadratic(functions.size() == 1 ? t/3D : t*0.5, beginMatrix)));
            //Point 2 to Point N-2 -> cubic function
            if(points.size() > 3) {
                int i = 1; //Point i to Point i+1
                while (i <= points.size() - 3) { //The last when i+3 == points.size()
                    double[][] matrix = MathUtils.matrixMultiply(cubicMatrix, makeMatrix(points.subList(i - 1, i + 3))); //i-1, i, i+1, i+2
                    functions.add(t -> new XY(executeCubic(t, matrix)));
                    i++;
                }
                //Point N-1 to Point N -> quadratic function
                double[][] endMatrix = MathUtils.matrixMultiply(quadraticMatrix, makeMatrix(points.subList(points.size() - 3, points.size())));
                functions.add(t -> new XY(executeQuadratic(t*0.5D+0.5D, endMatrix)));
            }
        }
        return (index, t) -> {
            if(index < functions.size() || (index == functions.size() && t == 0)) {
                return functions.get(index).apply(t);
            }
            return null;
        };
    }

    private static double executeQuadratic(double t, double a, double b, double c) {
        return (a * t + b) * t + c;
    }

    private static double[] executeQuadratic(double t, double[][] matrix) {
        return new double[] { executeQuadratic(t, matrix[0][0], matrix[1][0], matrix[2][0]), executeQuadratic(t, matrix[0][1], matrix[1][1], matrix[2][1]) };
    }

    private static double executeCubic(double t, double a, double b, double c, double d) {
        return (((a * t + b) * t + c) * t + d) * 0.5D;
    }

    private static double[] executeCubic(double t, double[][] matrix) {
        return new double[] { executeCubic(t, matrix[0][0], matrix[1][0], matrix[2][0], matrix[3][0]), executeCubic(t, matrix[0][1], matrix[1][1], matrix[2][1], matrix[3][1]) };
    }

    private static double[][] makeMatrix(List<XY> points) {
        double[][] result = new double[points.size()][2];
        for (int i = 0; i < points.size(); i++) {
            result[i] = points.get(i).toArray();
        }
        return result;
    }
}

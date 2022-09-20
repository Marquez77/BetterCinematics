package me.marquez.bettercinematics;

import me.marquez.bettercinematics.utils.MathUtils;
import me.marquez.bettercinematics.utils.SplineUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Function;

public class MathTest {

    static double[][] expression = new double[][] {
            {-1, 3, -3, 1},
            {2, -5, 4, -1},
            {-1, 0, 1, 0},
            {0, 2, 0, 0}
    };

    public static void main(String[] args) {
//        double[][] a = new double[][] {
//                {0, 0},
//                {3, 5},
//                {6, 2},
//                {9, 8}
//        };
//        printMatrix(a);
//
//        printMatrix(expression);
//
//        double[][] result = MathUtils.matrixMultiply(expression, a);
//        printMatrix(result);

        BufferedImage image = new BufferedImage(1324,1024, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = (Graphics2D)image.getGraphics();
        graphic.setColor(Color.BLACK);
        graphic.fillRect(10, 10, 1304, 1004);
        graphic.setColor(Color.WHITE);
        graphic.fillRect(15, 15, 1294, 994);
        graphic.setColor(Color.BLUE);
        java.util.List<SplineUtils.XY> array = Arrays.asList(new SplineUtils.XY(0D, 0D), new SplineUtils.XY(3D, 5D), new SplineUtils.XY(5D, 3D), new SplineUtils.XY(7D, 7D), new SplineUtils.XY(9D, 1D));
        Function<Double, SplineUtils.XY> function = SplineUtils.getCubicSpline(array);
        double t = 0D;
        while(t < 4D) {
            SplineUtils.XY xy = function.apply(t);
            if(xy != null) {
                double x = xy.getFirst();
                double y = xy.getSecond();
                graphic.fillOval((int) (x * 100D) + 100, (int) (y * 100D) + 100, 3, 3);
            }
            t += 0.001;
        }
        graphic.setColor(Color.RED);
        for (SplineUtils.XY xy : array) {
            double[] doubles = xy.toArray();
            graphic.fillOval((int) (doubles[0] * 100D)+96, (int) (doubles[1] * 100D)+96, 11, 11);
        }

        JFrame frame = new JFrame("TEST") {
            @Override
            public void paint(Graphics g) {
                g.drawImage(image, 0, 10, null);
            }
        };
        frame.setSize(1324, 1024);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    static double p(double t, double a, double b, double c) {
        return (a * t + b) * t + c;
    }

    static double p2(double t, double a, double b, double c, double d) {
        return (((a * t + b) * t + c) * t + d) * 0.5D;
    }

    private static void printMatrix(double[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

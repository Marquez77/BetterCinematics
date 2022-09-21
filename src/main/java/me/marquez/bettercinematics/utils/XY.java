package me.marquez.bettercinematics.utils;

import com.mojang.datafixers.util.Pair;

public class XY extends Pair<Double, Double> {
    public XY(double first, double second) {
        super(first, second);
    }

    public XY(double[] array) {
        this(array[0], array[1]);
    }

    public double[] toArray() {
        return new double[] { getFirst(), getSecond() };
    }

    public static XY of(double first, double second) {
        return new XY(first, second);
    }

}

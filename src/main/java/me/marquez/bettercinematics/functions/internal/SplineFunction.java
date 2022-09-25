package me.marquez.bettercinematics.functions.internal;

import me.marquez.bettercinematics.functions.CinematicFunction;
import me.marquez.bettercinematics.utils.SplineUtils;
import me.marquez.bettercinematics.utils.XY;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SplineFunction extends CinematicFunction {

    public final boolean isValid;
    private final BiFunction<Integer, Double, XY> xzFunction;
    private final BiFunction<Integer, Double, XY> yFunction;

    public SplineFunction(List<Location> points) {
        super(points);
        if(points.size() > 2) {
            xzFunction = SplineUtils.getCubicSpline(points.stream().map(loc -> XY.of(loc.getX(), loc.getZ())).toList());
            List<XY> list = new ArrayList<>();
            for (int i = 0; i < points.size(); i++) {
                list.add(XY.of(i, points.get(i).getY()));
            }
            yFunction = SplineUtils.getCubicSpline(list);
            isValid = true;
        }else {
            xzFunction = yFunction = (index, rate) -> XY.of(0D, 0D);
            isValid = false;
        }
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public Location applyFunction(int index, double rate) {
        XY pos = xzFunction.apply(index, rate);
        double y = yFunction.apply(index, rate).getSecond();
        return new Location(world, pos.getFirst(), y, pos.getSecond());
    }
}

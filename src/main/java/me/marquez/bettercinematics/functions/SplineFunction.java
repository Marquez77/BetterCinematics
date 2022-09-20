package me.marquez.bettercinematics.functions;

import me.marquez.bettercinematics.utils.SplineUtils;
import org.bukkit.Location;

import java.util.List;
import java.util.function.Function;

public class SplineFunction implements PathFunction {

    Function<Double, SplineUtils.XY> splineFunction;

    List<Location> position;

    public SplineFunction(List<Location> position) {
        this.position = position;
        splineFunction = SplineUtils.getCubicSpline(position.stream().map(loc -> new SplineUtils.XY(loc.getX(), loc.getZ())).toList());
    }


    @Override
    public List<Location> getAllLine(double pointPerBlock) {
        return null;
    }

    @Override
    public List<Location> getAllLineOfDuration(long interval, long duration) {
        return null;
    }

    @Override
    public Location apply(Double aDouble) {
        SplineUtils.XY pos = splineFunction.apply(aDouble);
        int index = (int)aDouble.doubleValue();
        double y = position.get(index).getY();
        y = y+(position.get(index+1).getY()-y)*(aDouble-index);
        return new Location(position.get(0).getWorld(), pos.getFirst(), y, pos.getSecond());
    }
}

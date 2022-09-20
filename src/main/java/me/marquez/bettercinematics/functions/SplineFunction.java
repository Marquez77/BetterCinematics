package me.marquez.bettercinematics.functions;

import me.marquez.bettercinematics.utils.SplineUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SplineFunction implements PathFunction {

    Function<Double, SplineUtils.XY> xzFunction;
    Function<Double, SplineUtils.XY> yFunction;

    List<Location> position;

    public SplineFunction(List<Location> position) {
        this.position = position;
        xzFunction = SplineUtils.getCubicSpline(position.stream().map(loc -> new SplineUtils.XY(loc.getX(), loc.getZ())).toList());
        List<SplineUtils.XY> list = new ArrayList<>();
        for(int i = 0; i < position.size(); i++) {
            list.add(new SplineUtils.XY((double)i, position.get(i).getY()));
        }
        yFunction = SplineUtils.getCubicSpline(list);
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
        SplineUtils.XY pos = xzFunction.apply(aDouble);
        double y = yFunction.apply(aDouble).getSecond();
        return new Location(position.get(0).getWorld(), pos.getFirst(), y, pos.getSecond());
    }
}

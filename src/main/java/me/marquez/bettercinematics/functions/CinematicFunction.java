package me.marquez.bettercinematics.functions;

import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CinematicFunction implements PathFunction {

    @Getter
    protected List<Location> points;

    protected CinematicFunction(List<Location> points) {
        this.points = points;
    }

    //t: 0 ~ points.size()-1
    abstract Location applyFunction(double t);

    @Override
    public Location apply(Double aDouble) {
        double t = aDouble;
        if(t < 0 || t > points.size()-1) return null; //Out of range
        Location result = applyFunction(t);
        int index = (int)t;
        float ratio = (float)(t-index);
        Location from = points.get(index);
        Location to = points.get(index+1);
        result.setYaw(from.getYaw()+(to.getYaw()-from.getYaw())*ratio);
        result.setPitch(from.getPitch()+(to.getPitch()-from.getPitch())*ratio);
        return result;
    }

    private final Map<Double, List<Location>> locationCache = new HashMap<>();
    @Override
    public List<Location> getAllLine(double interval) {
        return locationCache.computeIfAbsent(interval, k -> {
            List<Location> locations = new ArrayList<>();
            for(double t = 0; t < points.size()-1; t += interval) {
                locations.add(applyFunction(t));
            }
            return locations;
        });
    }

    @Override
    public List<Location> getAllLineOfDuration(long interval, long duration) {
        return getAllLine((points.size()-1)/((double)duration/interval));
    }
}

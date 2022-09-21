package me.marquez.bettercinematics.functions;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class CinematicFunction implements PathFunction {

    @NonNull
    protected List<Location> points;

    @Nullable
    protected World world;

    protected CinematicFunction(@NonNull List<Location> points) {
        this.points = points;
        if(!points.isEmpty()) {
            world = points.get(0).getWorld();
        }
    }

    protected abstract Location applyFunction(int index, double rate);

    @Override
    public Location apply(Double aDouble) { //t: 0 ~ points.size()-1
        double t = aDouble;
        if(t < 0 || t > points.size()-1) return null; //Out of range
        int index = (int)(t-0.00000001D); //to make 3, 0 -> 2, 1
        float rate = (float)(t-index);
        Location result = applyFunction(index, rate);
        Location from = points.get(index);
        Location to = points.get(index+1);
        result.setYaw(from.getYaw()+(to.getYaw()-from.getYaw())*rate);
        result.setPitch(from.getPitch()+(to.getPitch()-from.getPitch())*rate);
        return result;
    }

    private final Map<Double, List<Location>> locationCache = new HashMap<>();
    @Override
    public List<Location> getAllLine(double interval) {
        return locationCache.computeIfAbsent(interval, k -> {
            List<Location> locations = new ArrayList<>();
            for (double t = 0D; t < points.size()-1; t += interval) {
                locations.add(apply(t));
            }
            locations.add(apply((double)(points.size()-1)));
            return locations;
        });
    }

    @Override
    public List<Location> getAllLineOfDuration(long interval, long duration) {
        return getAllLine((points.size()-1)/((double)duration/interval));
    }
}

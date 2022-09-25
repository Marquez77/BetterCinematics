package me.marquez.bettercinematics.functions.internal;

import me.marquez.bettercinematics.functions.CinematicFunction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LinearFunction extends CinematicFunction {

    private final List<Vector> vectors; //point to point direction vector

    public LinearFunction(List<Location> points) {
        super(points);
        this.vectors = new ArrayList<>();
        if (points.size() > 1) {
            for (int i = 0; i < points.size() - 1; i++) {
                Location from = points.get(i);
                Location to = points.get(i + 1);
                vectors.add(to.clone().subtract(from).toVector());
            }
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Location applyFunction(int index, double rate) {
        Location fromLoc = points.get(index);
        return fromLoc.clone().add(vectors.get(index).clone().multiply(rate));
    }
}

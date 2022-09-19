package me.marquez.bettercinematics.functions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class LinearFunction implements Function<Double, Location> {

    private final List<Location> points;
    private final List<Double> sections;
    private final List<Vector> vectors; //point to point distance vector

    public LinearFunction(List<Location> points) {
        this.points = points;
        sections = new ArrayList<>();
        sections.add(0D);
        vectors = new ArrayList<>();
        if(points.size() > 1) {
            double sum = 0D;
            for (int i = 0; i < points.size()-1; i++) {
                Location from = points.get(i);
                Location to = points.get(i+1);
                double distance = from.distance(to);
                sum += distance;
                sections.add(sum);
                vectors.add(to.clone().subtract(from).toVector());
            }
        }
    }

    public Set<Location> getAllLine(double pointPerBlock) {
        Set<Location> locations = new HashSet<>();
        for(double d = 0; d < sections.get(sections.size()-1); d += pointPerBlock) {
            locations.add(apply(d));
        }
        return locations;
    }

    @Override
    public Location apply(Double t) {
        int i = sections.size()-1;
        for(; i > 0 && t < sections.get(i); i--);
        if(i == sections.size()-1) return null; //Over maximum
        double from = sections.get(i);
        double to = sections.get(i+1);
        double distance = to-from;
        double ratio = (t-from)/distance;
        return points.get(i).clone().add(vectors.get(i).clone().multiply(ratio));
    }
}

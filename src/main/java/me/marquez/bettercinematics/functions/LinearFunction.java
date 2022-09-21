package me.marquez.bettercinematics.functions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LinearFunction extends CinematicFunction {

    private final List<Double> sections;
    private final List<Vector> vectors; //point to point distance vector
    private final double totalSection;

    public LinearFunction(List<Location> points) {
        this.points = points;
        sections = new ArrayList<>();
        sections.add(0D);
        vectors = new ArrayList<>();
        if (points.size() > 1) {
            double sum = 0D;
            for (int i = 0; i < points.size() - 1; i++) {
                Location from = points.get(i);
                Location to = points.get(i + 1);
                double distance = from.distance(to);
                sum += distance;
                sections.add(sum);
                vectors.add(to.clone().subtract(from).toVector());
            }
            totalSection = sum;
        } else {
            totalSection = 0;
        }
    }

    @Override
    public Location applyFunction(double t) {
        int i = sections.size()-1;
        for(; i > 0 && t < sections.get(i); i--);
        if(i == sections.size()-1) return null; //Over maximum
        double from = sections.get(i);
        double to = sections.get(i+1);
        double ratio = (t-from)/(to-from);
        Location fromLoc = points.get(i);
        Location toLoc = points.get(i+1);
        return fromLoc.clone().add(vectors.get(i).clone().multiply(ratio));
    }
}

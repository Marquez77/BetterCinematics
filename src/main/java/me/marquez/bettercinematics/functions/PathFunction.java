package me.marquez.bettercinematics.functions;

import org.bukkit.Location;

import java.util.List;
import java.util.function.Function;

public interface PathFunction extends Function<Double, Location> {

    List<Location> getAllLine(double pointPerBlock);
    List<Location> getAllLineOfDuration(long interval, long duration);

}

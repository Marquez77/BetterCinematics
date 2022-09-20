package me.marquez.bettercinematics.entity;

import lombok.*;
import me.marquez.bettercinematics.functions.LinearFunction;
import org.bukkit.Location;

import java.util.function.Function;

@Getter
public class CalculatedCinematic extends Cinematic{

    @Setter
    private LinearFunction lineFunction;
    @Setter
    private Function<Double, Location> splineFunction;

    public CalculatedCinematic(@NonNull String name) {
        super(name);
    }

    public Function<Double, Location> getFunction() {
        return splineFunction == null ? lineFunction : splineFunction;
    }
}

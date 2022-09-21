package me.marquez.bettercinematics.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marquez.bettercinematics.functions.internal.LinearFunction;
import me.marquez.bettercinematics.functions.PathFunction;

@Getter
@Setter
public class CalculatedCinematic extends Cinematic{

    private LinearFunction lineFunction;
    private PathFunction splineFunction;

    public CalculatedCinematic(@NonNull String name) {
        super(name);
    }

    public PathFunction getPathFunction() {
        return splineFunction == null ? lineFunction : splineFunction;
    }
}

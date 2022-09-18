package me.marquez.bettercinematics.entity;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.List;

public class CalculatedCinematic extends Cinematic{

    private List<Location> vectors;

    public CalculatedCinematic(@NonNull String name) {
        super(name);
    }
}

package me.marquez.bettercinematics.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@RequiredArgsConstructor
public class Scene {

    @NonNull
    private Location from;
    @NonNull
    private Location to;
    @Setter
    private double speed;


}

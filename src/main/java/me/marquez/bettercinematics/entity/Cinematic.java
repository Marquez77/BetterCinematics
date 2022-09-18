package me.marquez.bettercinematics.entity;

import lombok.Getter;
import org.bukkit.Location;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Cinematic {

    private boolean enabled;
    private String name;
    private List<Scene> sceneList;
    private CinematicMode mode;
    private boolean freeAngle;

    public Set<Location> getPositions() {
        return sceneList.stream().flatMap(scene -> Stream.of(scene.getFrom(), scene.getTo())).collect(Collectors.toSet());
    }

}

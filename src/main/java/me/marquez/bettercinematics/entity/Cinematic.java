package me.marquez.bettercinematics.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Cinematic {

    private boolean enabled;
    @NonNull
    private String name;
    private List<Scene> sceneList;
    private CinematicMode mode;
    private boolean freeAngle;

    public Cinematic(@NonNull String name) {
        this.enabled = true;
        this.name = name;
        this.sceneList = new LinkedList<>();
        this.mode = CinematicMode.PRIMITIVE;
        this.freeAngle = false;
    }

    public Set<Location> getPositions() {
        return sceneList.stream().flatMap(scene -> Stream.of(scene.getFrom(), scene.getTo())).collect(Collectors.toSet());
    }
}

package me.marquez.bettercinematics.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.marquez.bettercinematics.entity.wrapper.WrappedLocation;
import me.marquez.bettercinematics.functions.LinearFunction;
import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@ToString
public class Cinematic {

    protected boolean enabled;
    @NonNull
    private String name;
    protected List<Scene> sceneList;
    protected CinematicMode mode;
    protected boolean freeAngle;

    public Cinematic(@NonNull String name) {
        this.enabled = true;
        this.name = name;
        this.sceneList = new LinkedList<>();
        this.mode = CinematicMode.PRIMITIVE;
        this.freeAngle = false;
    }

    public Set<WrappedLocation> getRawPositions() {
        return sceneList.stream().flatMap(scene -> Stream.of(scene.getFrom(), scene.getTo())).collect(Collectors.toSet());
    }

    public List<Location> getPositions() {
        return sceneList.stream().flatMap(scene -> Stream.of(scene.getFrom().toBukkitLocation(), scene.getTo().toBukkitLocation())).toList();
    }

    public CalculatedCinematic calculate() {
        CalculatedCinematic cinematic = new CalculatedCinematic(name);
        cinematic.enabled = enabled;
        cinematic.sceneList = sceneList;
        cinematic.mode = mode;
        cinematic.freeAngle = freeAngle;
        cinematic.setLineFunction(new LinearFunction(getPositions()));
        return cinematic;
    }
}

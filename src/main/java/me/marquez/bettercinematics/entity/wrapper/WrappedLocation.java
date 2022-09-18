package me.marquez.bettercinematics.entity.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;

@Data
@ToString
@AllArgsConstructor
public class WrappedLocation {
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    @Nullable
    public Location toBukkitLocation() {
        World world = Bukkit.getWorld(worldName);
        return world == null ? null : new Location(world, x, y, z, yaw, pitch);
    }

    public static WrappedLocation of(Location location) {
        World world = location.getWorld();
        return new WrappedLocation(world == null ? null : world.getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}

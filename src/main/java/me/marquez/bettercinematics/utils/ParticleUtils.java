package me.marquez.bettercinematics.utils;

import lombok.Builder;
import lombok.NonNull;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

@Builder
public class ParticleUtils {

    @NonNull
    private Particle particle;
    private int count = 1;
    private double offsetX = 0D;
    private double offsetY = 0D;
    private double offsetZ = 0D;
    private double extra = 0D; //normally speed
    private Object data = null;

    public void showParticle(org.bukkit.Location location, Player player) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

}
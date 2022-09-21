package me.marquez.bettercinematics.players.internal;

import lombok.NonNull;
import me.marquez.bettercinematics.BetterCinematics;
import me.marquez.bettercinematics.entity.CalculatedCinematic;
import me.marquez.bettercinematics.entity.PlayOptions;
import me.marquez.bettercinematics.players.CachedCinematicPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PrimitivePlayer extends CachedCinematicPlayer {

    private final Map<Player, Integer> taskIds = new HashMap<>();

    @Override
    public @NonNull CompletableFuture<Location> play(@NonNull CalculatedCinematic cinematic, @NonNull Player player, PlayOptions options) {
        if(taskIds.containsKey(player)) stop(player);
        CompletableFuture<Location> future = new CompletableFuture<>();
        List<Location> locations = cinematic.getPathFunction().getAllLineOfDuration(options.getInterval(), options.getDuration());
        Iterator<Location> iterator = locations.iterator();
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(BetterCinematics.getInstance(), () -> {
            if(iterator.hasNext()) {
                Location loc = iterator.next();
                player.teleport(loc);
            }else {
                stop(player); //stop 할 때 CompletableFuture 에서 Exception 발생하도록 추가 개발.
                future.complete(player.getLocation());
            }
        }, 0L, options.getInterval()/50L);
        taskIds.put(player, taskId);
        return future;
    }

    @Override
    public boolean stop(@NonNull Player player) {
        removePlayingCinematic(player);
        Optional.ofNullable(taskIds.get(player)).ifPresent(taskId -> Bukkit.getScheduler().cancelTask(taskId));
        return true;
    }
}

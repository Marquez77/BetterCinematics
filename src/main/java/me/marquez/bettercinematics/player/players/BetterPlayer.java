package me.marquez.bettercinematics.player.players;

import lombok.NonNull;
import me.marquez.bettercinematics.entity.CalculatedCinematic;
import me.marquez.bettercinematics.entity.PlayOptions;
import me.marquez.bettercinematics.player.CachedCinematicPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class BetterPlayer extends CachedCinematicPlayer {

    @Override
    public @NonNull CompletableFuture<Location> play(@NonNull CalculatedCinematic cinematic, @NonNull Player player, PlayOptions options) {
        return null;
    }
}

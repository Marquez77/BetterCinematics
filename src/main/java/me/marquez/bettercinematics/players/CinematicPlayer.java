package me.marquez.bettercinematics.players;

import lombok.NonNull;
import me.marquez.bettercinematics.entity.CalculatedCinematic;
import me.marquez.bettercinematics.entity.Cinematic;
import me.marquez.bettercinematics.entity.PlayOptions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public interface CinematicPlayer {

    default boolean isPlaying(@NonNull Player player) {
        return getPlayingCinematic(player) != null;
    }

    @Nullable
    Cinematic getPlayingCinematic(@NonNull Player player);

    @NonNull
    CompletableFuture<Location> play(@NonNull CalculatedCinematic cinematic, @NonNull Player player, PlayOptions options);

    boolean stop(@NonNull Player player);

}

package me.marquez.bettercinematics.players;

import lombok.NonNull;
import me.marquez.bettercinematics.entity.Cinematic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CachedCinematicPlayer implements CinematicPlayer {

    private final Map<Player, Cinematic> playerMap = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public Cinematic getPlayingCinematic(@NonNull Player player) {
        return playerMap.get(player);
    }

    public void putPlayingCinematic(@NonNull Player player, @NonNull Cinematic cinematic) {
        playerMap.compute(player, (k, v) -> {
            stop(player);
            return cinematic;
        });
    }

    public Cinematic removePlayingCinematic(Player player) {
        return playerMap.remove(player);
    }

    @Override
    public boolean stop(@NonNull Player player) {
        return removePlayingCinematic(player) != null;
    }
}

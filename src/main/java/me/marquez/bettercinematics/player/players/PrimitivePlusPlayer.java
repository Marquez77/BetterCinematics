package me.marquez.bettercinematics.player.players;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.NonNull;
import me.marquez.bettercinematics.BetterCinematics;
import me.marquez.bettercinematics.entity.CalculatedCinematic;
import me.marquez.bettercinematics.entity.PlayOptions;
import me.marquez.bettercinematics.player.CachedCinematicPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PrimitivePlusPlayer extends CachedCinematicPlayer {

    private final Map<Player, CompletableFuture<Location>> playingMap = new HashMap<>();

    private PrimitivePlusPlayer() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(BetterCinematics.getInstance(), PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(playingMap.containsKey(event.getPlayer())) {
                    event.setCancelled(true);
                }
            }
        });
    }

    @Override
    public @NonNull CompletableFuture<Location> play(@NonNull CalculatedCinematic cinematic, @NonNull Player player, PlayOptions options) {
        if(playingMap.containsKey(player)) stop(player);
        CompletableFuture<Location> future = new CompletableFuture<>();

        return future;
    }

    @Override
    public boolean stop(@NonNull Player player) {
        playingMap.remove(player);
        return true;
    }
}

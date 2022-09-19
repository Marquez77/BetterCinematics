package me.marquez.bettercinematics.preview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.marquez.bettercinematics.entity.Cinematic;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class CinematicPreview extends Thread {

    @NonNull
    private Player viewer;
    @NonNull
    private Cinematic cinematic;
    private Set<PreviewType> types = PreviewTypeRegistry.getAllTypes();

    @Override
    public void run() {
        try {
            while (!isInterrupted() && viewer.isOnline() && cinematic.isEnabled()) {
                types.forEach(type -> type.playEffect(cinematic, viewer));
                Thread.sleep(50); //every 1 tick
            }
        }catch(InterruptedException ignored) {}
    }
}

package me.marquez.bettercinematics.preview;

import me.marquez.bettercinematics.entity.Cinematic;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public interface PreviewType {

    String getName();
    String getAlias();

    void playEffect(Cinematic cinematic, Player player);

}

package me.marquez.bettercinematics.preview;

import me.marquez.bettercinematics.entity.Cinematic;
import org.bukkit.entity.Player;

public interface PreviewType {

    String getName();
    String getAlias();

    void playEffect(Cinematic cinematic, Player player);

}

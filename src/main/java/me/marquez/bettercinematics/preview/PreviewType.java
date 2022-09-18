package me.marquez.bettercinematics.preview;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marquez.bettercinematics.entity.Cinematic;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public interface PreviewType {

    static final Set<PreviewType> ALL = new HashSet<>();

    String getName();
    String getAlias();

    void playEffect(Cinematic cinematic, Player player);

    static PreviewType of(String nameOrAlias) {
        return ALL.stream().filter(e -> e.getAlias().equalsIgnoreCase(nameOrAlias) || e.getName().equals(nameOrAlias)).findAny().orElse(null);
    }

}

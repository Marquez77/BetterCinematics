package me.marquez.bettercinematics.preview;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marquez.bettercinematics.entity.Cinematic;
import me.marquez.bettercinematics.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

@Getter
@Setter
public class BasicPreviewType implements PreviewType {
    @NonNull
    private String name;
    private String alias;
    private BiConsumer<Cinematic, Player> playEffect;

    private BasicPreviewType(@NonNull String name, String alias, BiConsumer<Cinematic, Player> playEffect) {
        this.name = name;
        this.alias = alias;
        this.playEffect = playEffect;
        PreviewType.ALL.add(this);
    }

    @Override
    public void playEffect(Cinematic cinematic, Player player) {
        playEffect.accept(cinematic, player);
    }

    private static ParticleUtils POSITION_PARTICLE = ParticleUtils.builder()
            .particle(Particle.REDSTONE)
            .data(new Particle.DustOptions(Color.RED, 1F))
            .build();
    enum Types {
        POSITION("pos", (cinematic, player) -> {
            cinematic.getPositions().forEach(loc -> POSITION_PARTICLE.showParticle(loc.toBukkitLocation(), player));
        }), //SCENE POSITION (POINT)
        DIRECTION("dir", (cinematic, player) -> {}), //DIRECTION OF PLAYER LOOKS
        LINE("line", (cinematic, player) -> {}), //LINE OF POINT TO POINT
        CURVE("curve", (cinematic, player) -> {}); //THE CURVES APPLIED EASE

        @NonNull
        @Getter
        private final PreviewType instance;

        Types(String alias, BiConsumer<Cinematic, Player> playEffect) {
            instance = new BasicPreviewType(name(), alias, playEffect);
        }
    }
}

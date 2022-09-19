package me.marquez.bettercinematics.preview;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marquez.bettercinematics.entity.CalculatedCinematic;
import me.marquez.bettercinematics.entity.Cinematic;
import me.marquez.bettercinematics.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
    }

    @Override
    public void playEffect(Cinematic cinematic, Player player) {
        playEffect.accept(cinematic, player);
    }

    private static ParticleUtils POSITION_PARTICLE = ParticleUtils.builder()
            .particle(Particle.REDSTONE)
            .data(new Particle.DustOptions(Color.RED, 1F))
            .build();
    private static ParticleUtils LINE_PARTICLE = ParticleUtils.builder()
            .particle(Particle.REDSTONE)
            .data(new Particle.DustOptions(Color.BLUE, 1F))
            .build();
    enum Types {
        POSITION("pos", (cinematic, player) -> {
            cinematic.getPositions().forEach(loc -> POSITION_PARTICLE.showParticle(loc, player));
        }), //SCENE POSITION (POINT)
        LINE("line", (cinematic, player) -> {
            if(cinematic instanceof CalculatedCinematic cc) {
                cc.getLineFunction().getAllLine(0.5D).forEach(loc -> LINE_PARTICLE.showParticle(loc, player));
            }
        }), //LINE OF POINT TO POINT
        DIRECTION("dir", (cinematic, player) -> {}), //DIRECTION OF PLAYER LOOKS
        CURVE("curve", (cinematic, player) -> {}); //THE CURVES APPLIED EASE

        @NonNull
        @Getter
        private final PreviewType instance;

        Types(String alias, BiConsumer<Cinematic, Player> playEffect) {
            instance = new BasicPreviewType(name(), alias, playEffect);
        }
    }

    public static void initialize() {
        Arrays.stream(Types.values()).map(Types::getInstance).forEach(PreviewTypeRegistry::register);
    }
}

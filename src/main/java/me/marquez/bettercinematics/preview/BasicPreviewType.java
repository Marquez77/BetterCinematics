package me.marquez.bettercinematics.preview;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.marquez.bettercinematics.entity.CalculatedCinematic;
import me.marquez.bettercinematics.entity.Cinematic;
import me.marquez.bettercinematics.utils.ParticleUtils;
import me.marquez.bettercinematics.utils.SplineUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
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
            .data(new Particle.DustOptions(Color.RED, 8F))
            .count(5)
            .build();
    private static ParticleUtils LINE_PARTICLE = ParticleUtils.builder()
            .particle(Particle.REDSTONE)
            .data(new Particle.DustOptions(Color.BLUE, 2F))
            .build();
    private static ParticleUtils CURVE_PARTICLE = ParticleUtils.builder()
            .particle(Particle.REDSTONE)
            .data(new Particle.DustOptions(Color.LIME, 2F))
            .build();
    enum Types {
        POSITION("pos", (cinematic, player) -> {
            cinematic.getPositions().forEach(loc -> POSITION_PARTICLE.showParticle(loc, player));
        }), //SCENE POSITION (POINT)
        LINE("line", (cinematic, player) -> {
            if(cinematic instanceof CalculatedCinematic cc) {
                cc.getLineFunction().getAllLine(0.01D).forEach(loc -> LINE_PARTICLE.showParticle(loc, player));
                //PreviewOption 등 만들어서 여러가지 설정할 수 있게 하기
            }
        }), //LINE OF POINT TO POINT
        DIRECTION("dir", (cinematic, player) -> {}), //DIRECTION OF PLAYER LOOKS
        CURVE("curve", (cinematic, player) -> {
            if(cinematic instanceof CalculatedCinematic cc) {
                cc.getSplineFunction().getAllLine(0.01D).forEach(loc -> CURVE_PARTICLE.showParticle(loc, player));
            }
        }); //THE CURVES APPLIED EASE

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

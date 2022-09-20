package me.marquez.bettercinematics.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.marquez.bettercinematics.player.CinematicPlayer;
import me.marquez.bettercinematics.player.players.PrimitivePlayer;

@RequiredArgsConstructor
public enum CinematicMode {

    PRIMITIVE(new PrimitivePlayer()),
    BETTER(new PrimitivePlayer()),
    ULTRA(new PrimitivePlayer());

    @NonNull
    @Getter
    private final CinematicPlayer player;
}

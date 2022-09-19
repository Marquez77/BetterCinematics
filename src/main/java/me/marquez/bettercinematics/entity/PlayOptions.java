package me.marquez.bettercinematics.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayOptions {

    private long interval;
    private long duration;
}

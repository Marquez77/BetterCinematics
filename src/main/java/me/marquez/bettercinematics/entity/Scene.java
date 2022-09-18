package me.marquez.bettercinematics.entity;

import lombok.*;
import me.marquez.bettercinematics.entity.wrapper.WrappedLocation;

@Getter
@RequiredArgsConstructor
@ToString
public class Scene {

    @NonNull
    private WrappedLocation from;
    @NonNull
    private WrappedLocation to;
    @Setter
    private double speed;


}

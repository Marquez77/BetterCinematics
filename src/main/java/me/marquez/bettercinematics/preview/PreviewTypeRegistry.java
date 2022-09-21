package me.marquez.bettercinematics.preview;

import java.util.HashSet;
import java.util.Set;

public class PreviewTypeRegistry {

    private static final Set<PreviewType> types = new HashSet<>();

    public static boolean register(PreviewType type) {
        return types.add(type);
    }

    public static Set<PreviewType> getAllTypes() {
        return new HashSet<>(types);
    }

    public static PreviewType of(String nameOrAlias) {
        return types.stream().filter(e -> e.getAlias().equalsIgnoreCase(nameOrAlias) || e.getName().equalsIgnoreCase(nameOrAlias)).findAny().orElse(null);
    }
}

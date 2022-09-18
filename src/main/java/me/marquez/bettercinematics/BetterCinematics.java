package me.marquez.bettercinematics;

import me.marquez.bettercinematics.entity.Cinematic;
import me.marquez.bettercinematics.utils.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BetterCinematics extends JavaPlugin {

    private Map<String, Cinematic> cinematicMap = new HashMap<>();

    @Override
    public void onEnable() {
        File cinematicDirectory = new File(getDataFolder(), "cinematics");
        if(cinematicDirectory.exists()) {
            File[] files = cinematicDirectory.listFiles();
            if(files != null) {
                for (File cinematicFile : files) {
                    String filename = cinematicFile.getName();
                    getLogger().info("Loading cinematic file: " + filename);
                    FileUtils.loadFromJson(cinematicFile, Cinematic.class).whenComplete((cinematic, throwable) -> {
                        if (throwable == null && cinematic != null) {
                            cinematicMap.put(cinematic.getName(), cinematic);
                            getLogger().info("Loaded cinematic " + cinematic.getName());
                        }else {
                            getLogger().warning("Failed to load cinematic " + filename);
                        }
                    });
                }
            }
        }
    }
}

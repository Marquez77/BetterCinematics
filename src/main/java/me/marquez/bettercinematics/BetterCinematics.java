package me.marquez.bettercinematics;

import me.marquez.bettercinematics.entity.Cinematic;
import me.marquez.bettercinematics.entity.Scene;
import me.marquez.bettercinematics.entity.wrapper.WrappedLocation;
import me.marquez.bettercinematics.preview.BasicPreviewType;
import me.marquez.bettercinematics.preview.CinematicPreview;
import me.marquez.bettercinematics.utils.FileUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BetterCinematics extends JavaPlugin {

    private Map<String, Cinematic> cinematicMap = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("bcm").setExecutor(this);
        BasicPreviewType.initialize();
        File cinematicDirectory = new File(getDataFolder(), "cinematics");
        if(cinematicDirectory.exists()) {
            File[] files = cinematicDirectory.listFiles();
            if(files != null) {
                for (File cinematicFile : files) {
                    String filename = cinematicFile.getName();
                    getLogger().info("Loading cinematic file: " + filename);
                    try {
                        Cinematic cinematic = FileUtils.loadFromJson(cinematicFile, Cinematic.class);
                        cinematicMap.put(cinematic.getName(), cinematic);
                        getLogger().info("Loaded cinematic " + cinematic.getName());
                    }catch(IOException e) {
                        getLogger().warning("Failed to load cinematic " + filename);
                    }
                }
            }
        }
    }


    private final Map<String, Location> positions = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {

        }else {
            switch(args[0]) {
                case "create": {
                    Cinematic cinematic = new Cinematic(args[1]);
                    cinematicMap.put(cinematic.getName(), cinematic);
                    sender.sendMessage("Created " + cinematic.getName());
                    break;
                }
                case "delete": {
                    Optional.ofNullable(cinematicMap.remove(args[1]))
                        .ifPresentOrElse(
                            cinematic -> sender.sendMessage("Deleted " + cinematic.getName()),
                            () -> sender.sendMessage("Not exists " + args[1])
                        );
                    break;
                }
                case "pos": {
                    positions.put(sender.getName()+args[1], ((Player)sender).getLocation());
                    sender.sendMessage("Set pos " + args[1]);
                    break;
                }
                case "scene": {
                    Optional.ofNullable(cinematicMap.get(args[1])).ifPresentOrElse(cinematic -> {
                        Location from = positions.get(sender.getName()+"from");
                        Location to = positions.get(sender.getName()+"to");
                        if(from == null || to == null) {
                            sender.sendMessage("You need to set position first ");
                            return;
                        }
                        List<Scene> list = cinematic.getSceneList();
                        list.add(new Scene(WrappedLocation.of(from), WrappedLocation.of(to)));
                        sender.sendMessage("Added scene " + list.size());
                    }, () -> {
                        sender.sendMessage("Not exists " + args[1]);
                    });
                    break;
                }
                case "calc": {
                    cinematicMap.computeIfPresent(args[1], (k, cinematic) -> {
                        sender.sendMessage("Calculating...");
                       return cinematic.calculate();
                    });
                    sender.sendMessage("Calculated.");
                    break;
                }
                case "save": {
                    Optional.ofNullable(cinematicMap.get(args[1])).ifPresentOrElse(cinematic -> {
                        FileUtils.saveToJsonAsync(new File(getDataFolder(), "cinematics/" + cinematic.getName() + ".json"), Cinematic.class, cinematic).whenCompleteAsync((unused, throwable) -> {
                            if(throwable == null) {
                                sender.sendMessage("Successfully saved " + cinematic.getName());
                            }else {
                                throwable.printStackTrace();
                                sender.sendMessage("Failed to save " + cinematic.getName());
                            }
                        });
                    }, () -> {
                        sender.sendMessage("Not exists " + args[1]);
                    });
                    break;
                }
                case "list": {

                    cinematicMap.forEach((k, v) -> {
                        sender.sendMessage(v.toString());
                    });
                    break;
                }
                case "preview": {
                    if(args[1].equals("stop")) {
                        preview.interrupt();
                        sender.sendMessage("Stop preview");
                        break;
                    }
                    Cinematic cinematic = cinematicMap.get(args[1]);
                    preview = new CinematicPreview(((Player)sender), cinematic);
                    preview.start();
                    sender.sendMessage("Start preview");
                    break;
                }
            }
        }
        return true;
    }

    private CinematicPreview preview;
}

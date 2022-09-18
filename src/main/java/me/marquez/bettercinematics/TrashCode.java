package me.marquez.bettercinematics;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class TrashCode extends JavaPlugin {

    private final Map<UUID, Player> playing = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        getCommand("bcinematics").setExecutor(this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(playing.containsKey(event.getPlayer().getUniqueId())) {
                    getLogger().info("CANCELLED");
                    event.setCancelled(true);
                    return;
                }
                PacketContainer packet = event.getPacket();
                getLogger().info(packet.getModifier().getValues().toString());
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK,
                PacketType.Play.Client.LOOK,
                PacketType.Play.Server.CAMERA,
                PacketType.Play.Server.POSITION,
                PacketType.Play.Server.ENTITY_LOOK,
                PacketType.Play.Server.ENTITY_TELEPORT,
                PacketType.Play.Server.REL_ENTITY_MOVE,
                PacketType.Play.Server.REL_ENTITY_MOVE_LOOK
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                getLogger().info(event.getPacket().getType().name());
                getLogger().info(packet.getModifier().getValues().toString());
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                getLogger().info(event.getPacket().getType().name());
                getLogger().info(packet.getModifier().getValues().toString());
            }
        });
    }

    @Override
    public void onDisable() {

    }

    private final List<Location> locations = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(label + " add");
            sender.sendMessage(label + " start (ticks)");
        }else {
            switch(args[0]) {
            case "test": {
                Player player = (Player) sender;
                Location loc = player.getLocation();
                ByteBuf byteBuf = Unpooled.buffer(128);
                FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
                buf.writeVarInt(player.getEntityId());
                buf.writeDouble(loc.getX());
                buf.writeDouble(loc.getY());
                buf.writeDouble(loc.getZ());
                buf.writeByte((byte) ((int) (loc.getYaw() * 256.0F / 360.0F)));
                buf.writeByte((byte) ((int) (loc.getPitch() * 256.0F / 360.0F)));
                buf.writeBoolean(player.isOnGround());
                ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket(buf);
                ((CraftPlayer) player).getHandle().connection.send(packet);
                break;
            }
            case "add":
                locations.add(((Player)sender).getLocation());
                sender.sendMessage("Added");
                break;
            case "clear":
                locations.clear();
                sender.sendMessage("Cleared");
                break;
            case "start": {
                sender.sendMessage("Start");
                int ticks = Integer.parseInt(args[1]);
                List<Double> distances = new ArrayList<>();
                double sum = 0D;
                for (int i = 0; i < locations.size() - 1; i++) {
                    double distance = locations.get(i).distance(locations.get(i + 1));
                    sum += distance;
                    distances.add(distance);
                }
                List<Integer> intervals = new ArrayList<>();
                double finalSum = sum;
                distances.forEach(distance -> {
                    intervals.add((int) ((distance / finalSum) * ((double) ticks)));
                });
                getLogger().info(intervals.toString());
                Executors.newCachedThreadPool().submit(() -> {
                    sender.sendMessage("Start2");
                    for (int i = 0; i < locations.size() - 1; i++) {
                        play(getBetweenLocations(locations.get(i), locations.get(i + 1), intervals.get(i)), (Player) sender);
                    }
                    sender.sendMessage("End");
                });
                break;
            }
            case "start2": {
                sender.sendMessage("Start");
                int ticks = Integer.parseInt(args[1]);
                List<Double> distances = new ArrayList<>();
                double sum = 0D;
                for (int i = 0; i < locations.size() - 1; i++) {
                    double distance = locations.get(i).distance(locations.get(i + 1));
                    sum += distance;
                    distances.add(distance);
                }
                List<Integer> intervals = new ArrayList<>();
                double finalSum = sum;
                distances.forEach(distance -> {
                    intervals.add((int) ((distance / finalSum) * ((double) ticks)));
                });
                Player player = (Player) sender;
                Location start = locations.get(0);
                ArmorStand as = new ArmorStand((((CraftWorld) start.getWorld()).getHandle()), start.getX(), start.getY(), start.getZ());
                as.setInvisible(true);
                as.setNoGravity(true);
                ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(as);
                ((CraftPlayer) player).getHandle().connection.send(packet);
                as.passengers = ImmutableList.of(((CraftPlayer) player).getHandle());
                ClientboundSetPassengersPacket packet3 = new ClientboundSetPassengersPacket(as);
                ((CraftPlayer) player).getHandle().connection.send(packet3);
                ClientboundSetEntityDataPacket packet2 = new ClientboundSetEntityDataPacket(as.getId(), as.getEntityData(), true);
                ((CraftPlayer) player).getHandle().connection.send(packet2);
                Executors.newCachedThreadPool().submit(() -> {
                    playing.put(player.getUniqueId(), player);
                    System.out.println(playing);
                    sender.sendMessage("Start2");
                    for (int i = 0; i < locations.size() - 1; i++) {
                        play2(getBetweenLocations(locations.get(i), locations.get(i + 1), intervals.get(i)), (Player) sender, as);
                    }
                    ClientboundRemoveEntitiesPacket packet4 = new ClientboundRemoveEntitiesPacket(as.getId());
                    ((CraftPlayer) player).getHandle().connection.send(packet4);
                    sender.sendMessage("End");
                    playing.remove(player.getUniqueId());
                });
                break;
            }
            case "start3": {
                sender.sendMessage("Start");
                int ticks = Integer.parseInt(args[1]);
                List<Double> distances = new ArrayList<>();
                double sum = 0D;
                for (int i = 0; i < locations.size() - 1; i++) {
                    double distance = locations.get(i).distance(locations.get(i + 1));
                    sum += distance;
                    distances.add(distance);
                }
                List<Integer> intervals = new ArrayList<>();
                double finalSum = sum;
                distances.forEach(distance -> {
                    intervals.add((int) ((distance / finalSum) * ((double) ticks)));
                });
                Player player = (Player) sender;
                Location start = locations.get(0);
                Entity as = new Pig(EntityType.PIG, (((CraftWorld) start.getWorld()).getHandle()));
                as.setPos(start.getX(), start.getY(), start.getZ());
                as.setInvisible(true);
                as.setNoGravity(true);
                ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(as);
                ((CraftPlayer) player).getHandle().connection.send(packet);
                ClientboundSetEntityDataPacket packet2 = new ClientboundSetEntityDataPacket(as.getId(), as.getEntityData(), true);
                ((CraftPlayer) player).getHandle().connection.send(packet2);
                ClientboundSetCameraPacket packet3 = new ClientboundSetCameraPacket(as);
                ((CraftPlayer) player).getHandle().connection.send(packet3);
                Executors.newCachedThreadPool().submit(() -> {
                    playing.put(player.getUniqueId(), player);
                    System.out.println(playing);
                    sender.sendMessage("Start3");
                    for (int i = 0; i < locations.size() - 1; i++) {
                        play3(getBetweenLocations(locations.get(i), locations.get(i + 1), intervals.get(i)), (Player) sender, as);
                    }
                    ClientboundSetCameraPacket packet5 = new ClientboundSetCameraPacket(((CraftPlayer)player).getHandle());
                    ((CraftPlayer) player).getHandle().connection.send(packet5);
                    ClientboundRemoveEntitiesPacket packet4 = new ClientboundRemoveEntitiesPacket(as.getId());
                    ((CraftPlayer) player).getHandle().connection.send(packet4);
                    sender.sendMessage("End");
                    playing.remove(player.getUniqueId());
                });
                break;
            }
            default:

            }
        }
        return true;
    }

    private float to360Yaw(float yaw) {
        if(yaw < 0) {
            return 360+yaw; //0~180, -180~0 -> 0~360
        }
        return yaw;
    }

    private float toBukkitYaw(float yaw) {
        if(yaw > 180) {
            return 360-yaw;
        }
        return yaw;
    }

    private float plusYaw(float yaw, float yaw2) {
        return toBukkitYaw(to360Yaw(yaw)+to360Yaw(yaw2));
    }

    private float subYaw(float yaw, float yaw2) {
        return plusYaw(yaw, -yaw2);
    }

    private List<Location> getBetweenLocations(Location first, Location second, int ticks) {
        double spaceX = (second.getX()-first.getX())/(double)ticks;
        double spaceY = (second.getY()-first.getY())/(double)ticks;
        double spaceZ = (second.getZ()-first.getZ())/(double)ticks;
        float yaw = (second.getYaw()-first.getYaw())/(float)ticks;
        float pitch = (second.getPitch()-first.getPitch())/(float)ticks;
        List<Location> locs = new ArrayList<>();
        locs.add(first);
        for(int i = 0; i < ticks; i++) {
            Location loc = locs.get(locs.size()-1).clone().add(spaceX, spaceY, spaceZ);
            loc.setYaw(loc.getYaw()+yaw);
            loc.setPitch(loc.getPitch()+pitch);
            locs.add(loc);
        }
        return locs;
    }

    private void play(List<Location> locations, Player player) {
        getLogger().info(locations.toString());
        locations.forEach(loc -> {
            getLogger().info(loc.toString());
            Bukkit.getScheduler().runTask(this, () -> {
                try {
                    player.teleport(loc);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            });
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void play2(List<Location> locations, Player player, ArmorStand as) {
//        getLogger().info(locations.toString());
        locations.forEach(loc -> {
//            getLogger().info(loc.toString());
            try {
                as.setPos(loc.getX(), loc.getY(), loc.getZ());
                ClientboundTeleportEntityPacket packet2 = new ClientboundTeleportEntityPacket(as);
                ((CraftPlayer)player).getHandle().connection.send(packet2);
//                Bukkit.getScheduler().runTask(this, () -> {
//                    ((CraftPlayer)player).getHandle().setPos(loc.getX(), loc.getY(), loc.getZ());
                    Set<ClientboundPlayerPositionPacket.RelativeArgument> arguments = new HashSet<>();
                    arguments.add(ClientboundPlayerPositionPacket.RelativeArgument.X);
                    arguments.add(ClientboundPlayerPositionPacket.RelativeArgument.Y);
                    arguments.add(ClientboundPlayerPositionPacket.RelativeArgument.Z);
                    byte byteYaw = (byte) ((int) (loc.getYaw() * 256.0F / 360.0F));
                    byte bytePitch = (byte) ((int) (loc.getPitch() * 256.0F / 360.0F));
                    float yaw = byteYaw*(360.0F/256.0F);
                    float pitch = bytePitch*(360.0F/256.0F);
                    getLogger().info(loc.getPitch() + ", " + bytePitch + ", " + pitch+"");
                    ClientboundPlayerPositionPacket packet3 = new ClientboundPlayerPositionPacket(0, 0, 0, loc.getYaw(), loc.getPitch(), arguments, player.getEntityId(), false);
                    ((CraftPlayer)player).getHandle().connection.send(packet3);
//                });
            }catch(Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(25L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void play3(List<Location> locations, Player player, Entity as) {
//        getLogger().info(locations.toString());
        locations.forEach(loc -> {
//            getLogger().info(loc.toString());
            try {
                as.setPos(loc.getX(), loc.getY(), loc.getZ());
//                as.setYRot(loc.getYaw());
//                as.setXRot(loc.getPitch());
//                as.setHeadPose(new Rotations(loc.getPitch(), loc.getYaw(), 0F));
                ClientboundTeleportEntityPacket packet2 = new ClientboundTeleportEntityPacket(as);
                ((CraftPlayer)player).getHandle().connection.send(packet2);
//                ClientboundSetEntityDataPacket packet3 = new ClientboundSetEntityDataPacket(as.getId(), as.getEntityData(), true);
//                ((CraftPlayer)player).getHandle().connection.send(packet3);
//                ClientboundRotateHeadPacket packet4 = new ClientboundRotateHeadPacket(as, (byte)((int)(as.getYRot() * 256.0F / 360.0F)));
//                ((CraftPlayer)player).getHandle().connection.send(packet4);
//                ClientboundMoveEntityPacket.Rot packet5 = new ClientboundMoveEntityPacket.Rot(as.getId(), (byte)((int)(as.getYRot() * 256.0F / 360.0F)), (byte)((int)(as.getXRot() * 256.0F / 360.0F)), false);
//                ((CraftPlayer)player).getHandle().connection.send(packet5);
//                Bukkit.getScheduler().runTask(this, () -> {
//                    ((CraftPlayer)player).getHandle().setPos(loc.getX(), loc.getY(), loc.getZ());
//                Set<ClientboundPlayerPositionPacket.RelativeArgument> arguments = new HashSet<>();
//                arguments.add(ClientboundPlayerPositionPacket.RelativeArgument.X);
//                arguments.add(ClientboundPlayerPositionPacket.RelativeArgument.Y);
//                arguments.add(ClientboundPlayerPositionPacket.RelativeArgument.Z);
//                byte byteYaw = (byte) ((int) (loc.getYaw() * 256.0F / 360.0F));
//                byte bytePitch = (byte) ((int) (loc.getPitch() * 256.0F / 360.0F));
//                float yaw = byteYaw*(360.0F/256.0F);
//                float pitch = bytePitch*(360.0F/256.0F);
//                getLogger().info(loc.getPitch() + ", " + bytePitch + ", " + pitch+"");
//                ClientboundPlayerPositionPacket packet5 = new ClientboundPlayerPositionPacket(0, 0, 0, loc.getYaw(), loc.getPitch(), arguments, player.getEntityId(), false);
//                ((CraftPlayer)player).getHandle().connection.send(packet5);
//                });
            }catch(Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(25L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

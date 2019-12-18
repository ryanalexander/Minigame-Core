package net.blockcade.Arcade.Utils.GameUtils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NPC {

    EntityPlayer player;
    GameProfile profile;
    ArmorStand armorStand;
    private boolean built = false;
    private String name;
    Location location;
    World world;
    MinecraftServer server;

    public NPC(String name, Location location) {
        World world = location.getWorld();
        this.name=name;
        this.location=location;
        this.world=world;
        this.server=((CraftWorld)world).getHandle().getMinecraftServer();
        profile=new GameProfile(UUID.randomUUID(), ChatColor.stripColor(name.substring(0,8))+"-G");
    }

    public void setSkin(String texture, String signature){
        profile.getProperties().put("textures",new Property("textures",texture,signature));
    }

    public void buildPlayer() {

        Player player = ((Player)world.spawnEntity(location,EntityType.PLAYER));
        player.setCustomName(Text.format(name));
        player.setCustomNameVisible(true);
/*
        armorStand = ((ArmorStand)world.spawnEntity(location, EntityType.ARMOR_STAND));
        armorStand.setVisible(true);
        armorStand.setMarker(true);
        armorStand.setBasePlate(false);
        armorStand.setCustomNameVisible(false);
        this.player=new EntityPlayer(server,((CraftWorld) world).getHandle(),profile, new PlayerInteractManager(((CraftWorld) world).getHandle()));
        this.player.setCustomName(new ChatComponentText(Text.format(name)));
        this.player.setCustomNameVisible(true);
        //this.player.setLocation(location.getX(),location.getY(),location.getZ(),location.getYaw(),location.getPitch());
        */

        built=true;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void showFor(Player player) throws EntityNotBuiltException {
        /*
        if(!built)throw new EntityNotBuiltException("The player must be built prior to showing.");
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,this.player);
        PacketPlayOutNamedEntitySpawn target = new PacketPlayOutNamedEntitySpawn(this.player);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(info);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(target);*/
    }

    public class EntityNotBuiltException extends Throwable {
        EntityNotBuiltException(String message){
            super(message);
        }
    }
}

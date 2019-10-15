package net.blockcade.Arcade.Utils;

import fr.neatmonster.nocheatplus.NCPAPIProvider;
import fr.neatmonster.nocheatplus.NoCheatPlus;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
import fr.neatmonster.nocheatplus.hooks.NCPHook;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;
import net.blockcade.Arcade.Main;
import net.minecraft.server.v1_14_R1.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class NCPHandler implements Listener {

    public static ArrayList<Player> bans = new ArrayList<>();

    public NCPHandler() {
        NCPHookManager.addHook(CheckType.ALL, new NCPHook() {
            @Override
            public String getHookName() {
                return null;
            }

            @Override
            public String getHookVersion() {
                return null;
            }

            @Override
            public boolean onCheckFailure(CheckType checkType, Player player, IViolationInfo iViolationInfo) {
                if(bans.contains(player)){
                    return false;
                }
                switch(checkType){
                    case MOVING_SURVIVALFLY:
                        if(iViolationInfo.getTotalVl()>7){System.out.println(String.format("Banning %s for Flight",player.getName()));banPlayer(player);}
                        break;
                    case FIGHT:
                        if(iViolationInfo.getTotalVl()>10){System.out.println(String.format("Banning %s for PVP Hacks",player.getName()));banPlayer(player);}
                        break;
                    case BLOCKPLACE_SCAFFOLD:
                        if(iViolationInfo.getTotalVl()>10){System.out.println(String.format("Banning %s for Scaffold",player.getName()));banPlayer(player);}
                        break;
                        default:
                        System.out.println(String.format("[IGNORED] Check Type: %s Player: %s Level: %s",checkType.getName(),player.getName(),iViolationInfo.getAddedVl()+"/"+iViolationInfo.getTotalVl()));
                }
                return false;
            }
        });
    }

    public void banPlayer(Player player){
            bans.add(player);
            SQL sql = new SQL("mc2.stelch.gg",3306,"root","Garcia#02","games");
            ResultSet result = sql.query("SELECT `uuid` FROM `players` WHERE `username`='"+player.getName()+"' LIMIT 1;");
            try {
                result.first();
                sql.query(String.format("DELETE FROM `player_bans` WHERE `uuid`='%s';",result.getString("uuid")),true);
                sql.query(String.format("INSERT INTO `player_bans` (`uuid`,`reason`,`admin`,`banned_till`) VALUES ('%s','%s','%s','%s');",result.getString("uuid"),"Cheating or related","Guardian",0),true);
            } catch (SQLException ev) {
                System.out.println("Failed to get UUID of player");
                ev.printStackTrace();
            }
            sql.query(String.format("UPDATE `players` SET `banned`='1' WHERE `username`='%s';",player.getName()),true);
            sql.close();
            new BukkitRunnable(){
                @Override
                public void run() {
                    Bukkit.broadcastMessage(Text.format("&a[GUARDIAN CHEAT DETECTION]\n&7A player has been found to be using illegal modifications to their game, and will be removed shortly."));
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if(player.isOnline()){((CraftPlayer)player).getHandle().playerConnection.networkManager.close(new ChatMessage("§cBanned by Guardian"));}
                        }
                    }.runTaskLater(Main.getPlugin(Main.class),60L);
                }
            }.runTaskLater(Main.getPlugin(Main.class),(new Random().nextInt(60)*20));
            bans.add(player);
    }

}

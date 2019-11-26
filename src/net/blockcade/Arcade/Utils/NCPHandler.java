package net.blockcade.Arcade.Utils;

import me.rerere.matrix.api.MatrixAPI;
import me.rerere.matrix.api.events.PlayerViolationEvent;
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Utils.Formatting.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class NCPHandler implements Listener {

    ArrayList<Player> bans = new ArrayList<>();

    private enum VIOLATION {
        HITBOX(17),
        KILLAURA(70),
        SPEED(180),
        FLY(180),
        BADPACKETS(150),
        FASTUSE(5),
        FASTHEAL(7),
        BLOCK(10),
        SCAFFOLD(16),
        JESUS(20),
        INVENTORY(20),
        VELOCITY(30),
        INTERACT(8),
        PHASE(35),
        VEHICLE(17)
        ;
        int i;
        VIOLATION(int i){
            this.i=i;
        }
        public int getI() {
            return i;
        }
    };

    @EventHandler
    public void IViolation(PlayerViolationEvent event){
        if(bans.contains(event.getPlayer()))return;
            VIOLATION v = VIOLATION.valueOf(event.getHackType().name().toUpperCase());
            int violation_count = MatrixAPI.getViolations(event.getPlayer(),event.getHackType());
            if(v!=null){
            if(violation_count>=v.getI()){
                banPlayer(event.getPlayer(),event.getHackType().name()+"|"+violation_count+"|"+event.getMessage());
                System.out.println(String.format("System banning %s for %s",event.getPlayer().getName(),event.getHackType().name()));
            }else {
                System.out.println(String.format("VIOLATION %s for %s is not > %s - No ban applied",violation_count,event.getHackType().name(),v.getI()));
            }
        }else {
            System.out.println("System ignored "+event.getHackType().name());
        }
    }

    public void banPlayer(Player player, String banData){
            bans.add(player);
            SQL sql = new SQL("mc2.stelch.gg",3306,"root","Garcia#02","games");
            ResultSet result = sql.query("SELECT `uuid` FROM `players` WHERE `username`='"+player.getName()+"' LIMIT 1;");
            try {
                result.first();
                sql.query(String.format("DELETE FROM `player_bans` WHERE `uuid`='%s';",result.getString("uuid")),true);
                sql.query(String.format("INSERT INTO `player_bans` (`uuid`,`reason`,`admin`,`banned_till`,`autoban_data`) VALUES ('%s','%s','%s','%s','%s');",result.getString("uuid"),"Cheating or related","Guardian",0,banData),true);
            } catch (SQLException ev) {
                System.out.println("Failed to get UUID of player");
                ev.printStackTrace();
            }
            sql.query(String.format("UPDATE `players` SET `banned`='1' WHERE `username`='%s';",player.getName()),true);
            sql.close();
            new BukkitRunnable(){
                @Override
                public void run() {
                    Bukkit.broadcastMessage(Text.format("&a[GUARDIAN CHEAT DETECTION]\n&fA player has been found to be using illegal modifications to their game, and will be removed shortly."));
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if(player.isOnline())player.kickPlayer(Text.format("&cGuardian Cheat Detection"));
                        }
                    }.runTaskLater(Main.getPlugin(Main.class),60L);
                }
            }.runTaskLater(Main.getPlugin(Main.class),(new Random().nextInt(30)*20));
            bans.add(player);
    }

}

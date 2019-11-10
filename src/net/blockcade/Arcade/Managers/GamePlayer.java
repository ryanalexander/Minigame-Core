package net.blockcade.Arcade.Managers;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.EventManager.PlayerCombatLogEvent;
import net.blockcade.Arcade.Managers.EventManager.PlayerDeathEvent;
import net.blockcade.Arcade.Utils.Formatting.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class GamePlayer implements Listener {

    private static HashMap<Player,GamePlayer> GamePlayers=new HashMap<>();
    private static Game game;

    private Player player;

    private long experience = 0;
    private int level = 0;

    private int killstreak = 0;
    private int highest_killstreak = 0;

    private int kills=0;
    private int deaths=0;

    private long last_combat = 0;
    private GamePlayer last_combat_player;

    private boolean eliminate=false;

    public GamePlayer(Game game){this.game=game;}

    public GamePlayer(Player player){GamePlayers.put(player,this);this.player=player;}

    public boolean isCombat() { return !((last_combat-System.currentTimeMillis())<-10000);}
    public long getCombatTime() {return isCombat()?(10000+(last_combat-System.currentTimeMillis())):0;}
    public GamePlayer getCombatPlayer() {return last_combat_player;}

    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level=level;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public void addExp(int amount){
        this.experience=experience+amount;
    }

    public Player getPlayer() {
        return player;
    }

    private void addKill() {this.kills=this.kills+1;}
    private void addDeath() {this.deaths=this.deaths+1;}

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setEliminate(boolean eliminate) {
        this.eliminate = eliminate;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

    public void setHighestKillstreak(int highest_killstreak) {
        this.highest_killstreak = highest_killstreak;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public int getHighestKillstreak() {
        return highest_killstreak;
    }

    public void remove() {GamePlayers.remove(this.getPlayer());}

    // Player access methods
    public void sendMessage(String message){ Text.sendMessage(this.player,message, Text.MessageType.TEXT_CHAT);}
    public void sendActionBar(String message){ Text.sendMessage(this.player,message, Text.MessageType.ACTION_BAR);}
    public void sendTitle(String title){ Text.sendMessage(this.player,title, Text.MessageType.TITLE); }

    public void playSound(Sound sound, float volume, float pitch){this.player.playSound(this.player.getLocation(),sound,volume,pitch);}

    public double getHealth(){return this.player.getHealth();}
    public Location getLocation(){return this.player.getLocation();}

    public ItemStack[] getInventoryContents() {return getInventory().getContents();}
    public Inventory getInventory() {return this.player.getInventory();}

    public void teleport(GamePlayer player){teleport(player.getLocation());}
    public void teleport(double x, double y, double z){teleport(this.player.getWorld(),x,y,z);}
    public void teleport(World world, double x, double y, double z){teleport(new Location(world,x,y,z));}
    public void teleport(Location location){this.player.teleport(location);}


    // Handler events
    @EventHandler
    private void PlayerLeave(PlayerQuitEvent e) {
        if(e.getPlayer().equals(this.getPlayer())){
            if(isCombat()){
                Bukkit.getPluginManager().callEvent(new PlayerCombatLogEvent(this));
                for(ItemStack is : getInventoryContents()){
                    Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(),is);
                }
            }
        }
        remove();
    }

    @EventHandler
    public void PlayerDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            GamePlayer victom = GamePlayers.get((Player)event.getEntity());
            GamePlayer damager = GamePlayers.get((Player)event.getDamager());
            victom.last_combat=System.currentTimeMillis();
            damager.last_combat=System.currentTimeMillis();
            victom.last_combat_player=damager;
            damager.last_combat_player=victom;
        }
    }
    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event){
        GamePlayer victom = GamePlayers.get((Player)event.getPlayer());
        GamePlayer damager = GamePlayers.get((Player)event.getKiller());
        victom.last_combat=0;
        victom.setKillstreak(0);
        victom.addDeath();
        damager.addKill();
        damager.setKillstreak(damager.getKillstreak()+1);
    }
}

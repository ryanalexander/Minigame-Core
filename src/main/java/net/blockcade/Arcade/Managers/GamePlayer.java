/*
 *
 *
 *  © Stelch Software 2020, distribution is strictly prohibited
 *  Blockcade is a company of Stelch Software
 *
 *  Changes to this file must be documented on push.
 *  Unauthorised changes to this file are prohibited.
 *
 *  @author Ryan W
 * @since (DD/MM/YYYY) 18/1/2020
 */

package net.blockcade.Arcade.Managers;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Managers.AdvancementManager.Achievement;
import net.blockcade.Arcade.Managers.EventManager.PlayerCombatLogEvent;
import net.blockcade.Arcade.Managers.EventManager.PlayerDeathEvent;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameName;
import net.blockcade.Arcade.Varables.Ranks;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class GamePlayer implements Listener {

    private static HashMap<Player,GamePlayer> GamePlayers=new HashMap<>();
    private static Game game;

    private Player player;
    private String name;
    private UUID uuid;

    private Ranks rank=Ranks.MEMBER;

    private boolean independent;

    // Core Statistics (DO NOT EDIT)
    private int CORE_wins = 0;
    private int CORE_losses = 0;
    private int CORE_kills = 0;
    private int CORE_final_kills = 0;
    private int CORE_deaths = 0;

    private long experience = 0;

    private int killstreak = 0;
    private int highest_killstreak = 0;

    private int eliminations=0;

    private int kills=0;
    private int deaths=0;

    private long last_combat = 0;
    private GamePlayer last_combat_player=null;

    private TeamColors team=null;

    private boolean eliminate=false;

    private ArrayList<Achievement> achievements = new ArrayList<>();

    public GamePlayer(Game game, boolean independent){
        this.game=game;
        this.independent=independent;
    }

    public GamePlayer(Player player){
        GamePlayers.put(player,this);
        this.player=player;
        if(game.hasModule(GameModule.TEAMS)&&team==null&&game.TeamManager().hasTeam(player))team=game.TeamManager().getTeam(player);

        new BukkitRunnable(){
            @Override
            public void run() {
                BuildPlayer();

                if(!hasStatistic(game.getGameName())){
                    System.out.println("New player - Creating statistic");
                    Main.getSqlConnection().query(String.format("INSERT INTO `player_statistics` (player_uuid,game_enum) VALUES ('%s','%s');",player.getUniqueId(),game.getGameName().name()),true);
                }
            }
        }.runTaskAsynchronously(game.handler());
    }

    /**
     * Check if player is currently in combat with another player
     * @return boolean stating player combat status
     */
    public boolean isCombat() { return !((last_combat-System.currentTimeMillis())<-10000);}
    /**
     * Check time remaining until player is no longer in combat
     * @return Milliseconds remaining
     */
    public long getCombatTime() {return isCombat()?(10000+(last_combat-System.currentTimeMillis())):0;}
    /**
     * Check the last player in combat
     * @return GamePlayer from last combat
     */
    public GamePlayer getCombatPlayer() {return last_combat_player;}

    /**
     * Check player (Network) uuid
     * @return Player UUID object
     */
    public UUID getUuid() {
        return uuid;
    }

    public void addExp(int amount){
        checkLevelup(amount);
        this.experience=experience+amount;
    }

    public Player getPlayer() {
        return player;
    }

    private void addKill() {this.kills=this.kills+1;}
    private void addDeath() {this.deaths=this.deaths+1;}
    public void addElimination() {this.eliminations=this.eliminations+1;}

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

    public void setTeam(TeamColors team) {
        this.team = team;
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

    public int getEliminations() {
        return eliminations;
    }

    public int getHighestKillstreak() {
        return highest_killstreak;
    }

    public TeamColors getTeam() {
        if(team==null&&game.hasModule(GameModule.TEAMS)&&game.TeamManager().hasTeam(player))team=game.TeamManager().getTeam(player);
        return team;
    }

    public void remove() {GamePlayers.remove(this.getPlayer());}

    public int getCORE_deaths() {
        return CORE_deaths+deaths;
    }

    public int getCORE_final_kills() {
        return CORE_final_kills;
    }

    public int getCORE_kills() {
        return CORE_kills+kills;
    }

    public int getCORE_losses() {
        return CORE_losses;
    }

    public int getCORE_wins() {
        return CORE_wins;
    }

    public Ranks getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

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

    /**
     * Get player network-wide level
     * @return Player network wide level
     */
    public double getLevel() {
        return (int) (Math.floor(25 + Math.sqrt(725 * experience))/50);
    }
    public void checkLevelup(int experience){
        if(getLevel()!=((int) (Math.floor(25 + Math.sqrt(625 + 100 * experience+this.experience))/50))){
            // Player levelled up
            player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,0.5f,0.5f);
            sendMessage("&a&k]&r&m&a========⛃ &d&lLevel up! &m&a⛃========&a&k[&r\n" +
                    "\n" +
                    "&7");
        }
    }
    public int getExpToNextLevel() {
        int L = (int)getLevel()+1;
        return 25 * L * L - 25 * L;
    }

    public int getExperience() {
        return (int)experience;
    }

    public void giveAchievement(Achievement achievement){
        achievements.add(achievement);
    }
    public boolean hasAchieved(Achievement achievement){
        return achievements.contains(achievement);
    }

    public boolean hasStatistic(GameName game){
        ResultSet r = Main.getSqlConnection().query(String.format("SELECT * FROM `player_statistics` WHERE `player_uuid`='%s' AND `game_enum`='%s' LIMIT 1;", player.getUniqueId(), game.name()));
        try {
            r.first();
            CORE_wins = r.getInt("wins");
            CORE_losses = r.getInt("losses");
            CORE_kills = r.getInt("kills");
            CORE_final_kills = r.getInt("final_kills");
            CORE_deaths = r.getInt("deaths");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to cache statistic for "+player.getUniqueId());
        }
        return true;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        if(independent){
            new GamePlayer(e.getPlayer());
        }
    }

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

    private void BuildPlayer() {
        String query=String.format("SELECT * FROM `players` WHERE `username`='%s' LIMIT 1;",this.getPlayer().getName());
        ResultSet results = Main.getSqlConnection().query(query);
        try {
            while(results.next()){
                this.name=results.getString("username");
                this.rank=(Ranks.valueOf(results.getString("rank").toUpperCase()));
                this.experience=results.getInt("experience");
                this.uuid=(UUID.fromString(results.getString("uuid")));

                // TODO Hook into Spigot and override player UUID
            }
            return;
        }catch (SQLException e){
            System.out.println("| ---------------------------- |");
            System.out.println("|  This error was posted by GamePlayer.java BuildPlayer");
            e.printStackTrace();
            System.out.println("| ---------------------------- |");
        }
    }

    @EventHandler
    public void PlayerDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            GamePlayer victom = GamePlayers.get((Player)event.getEntity());
            GamePlayer damager = GamePlayers.get((Player)event.getDamager());
            if(victom!=null){
                victom.last_combat=System.currentTimeMillis();
                victom.last_combat_player=damager;
            }
            if(damager!=null) {
                damager.last_combat_player = victom;
                damager.last_combat = System.currentTimeMillis();
            }
        }
    }
    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event){
        GamePlayer victom = GamePlayers.get(event.getPlayer());
        victom.last_combat = 0;
        victom.setKillstreak(0);
        victom.addDeath();
        if(event.getKiller() instanceof Player) {
            GamePlayer damager = GamePlayers.get(event.getKiller());
            damager.addKill();
            damager.setKillstreak(damager.getKillstreak()+1);
        }
    }

    public static GamePlayer getGamePlayer(Player player){
        if(!GamePlayers.containsKey(player))return new GamePlayer(player);
        return GamePlayers.get(player);
    }

}

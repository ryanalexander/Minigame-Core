
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

package net.blockcade.Arcade.Events;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.EventManager.PlayerRespawnEvent;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

import static net.blockcade.Arcade.Varables.GameModule.MAX_DAMAGE_HEIGHT;
import static net.blockcade.Arcade.Varables.GameModule.NO_FALL_DAMAGE;
import static org.bukkit.event.entity.EntityPotionEffectEvent.Cause.ARROW;

public class playerDeathEvent implements Listener {

    private static Game game;

    public playerDeathEvent(Game game) {
        playerDeathEvent.game = game;
    }

    private static ArrayList<Entity> invulnerable = new ArrayList<>();

    public static String[] messages = new String[]{
            "%s&7 has been slain by %s",
            "%s&7 has been clapped by %s",
            "%s&7 has been wasted by %s",
            "%s&7 has been rekt by %s"
    };

    public static String[] fall_self = new String[]{
            "%s&7 fell and hit their head",
            "&7%s&7's balance was not up to standard",
            "%s&7 didn't notice the edge",
            "%s&7 is on the highway to hell",
            "%s&7 followed the path to &c&lTHE NETHER!"
    };
    public static String[] fall = new String[]{
            "%s&7 fell and hit their head with the assistance of %s",
            "&7%s&7 was lead to their death by %s",
            "%s&7 didn't notice %s&7 pushing them to their death",
            "%s&7 was given a rocky road to death by %s"
    };

    private String[] entity_explode = new String[]{
            "%s&7's trusty tnt blew %s&7 to smithereens",
            "&7Boom! %s&7 has blown up %s&7!"
    };

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if(!game.hasModule(GameModule.DEATH_MANAGER))return;
        e.setDeathMessage(null);
        e.setDroppedExp(0);
    }
    @EventHandler
    public void EntityDeath(EntityDeathEvent e){
        if(!game.hasModule(GameModule.DEATH_MANAGER))return;
        switch(e.getEntityType()){
            case SILVERFISH:
                e.getEntity().setAI(false);
                e.getEntity().setCustomNameVisible(false);
                e.setDroppedExp(0);
        }
    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player&&Spectator.isSpectator((Player)e.getDamager())) {
            e.setCancelled(true);
            return;
        }
        if (!(game.GameState().equals(GameState.IN_GAME))) return;
        if(game.hasModule(NO_FALL_DAMAGE)&&e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){e.setCancelled(true);return;}

        if(e.getEntityType().equals(EntityType.SILVERFISH)){
            if(((LivingEntity)e.getEntity()).getMaxHealth()-e.getDamage()<=1){
                e.setCancelled(true);
                String basestr = "";
                for (int i = 0; i < ((LivingEntity)e.getEntity()).getMaxHealth(); i++) {
                    basestr += ((i < ((LivingEntity)e.getEntity()).getHealth()-1) ? "&c" : "&c") + "&l&m▌";
                }
                e.getEntity().getWorld().playSound(e.getEntity().getLocation(),Sound.ENTITY_SILVERFISH_DEATH,1f,1f);
                e.getEntity().remove();
                return;
            }
            String basestr = "";
            for (int i = 0; i < ((LivingEntity)e.getEntity()).getMaxHealth(); i++) {
                basestr += ((i < ((LivingEntity)e.getEntity()).getHealth()-1) ? "&a" : "&7") + "&l&m▌";
            }
            e.getEntity().setCustomName(Text.format(basestr));
            e.getEntity().setCustomNameVisible(true);
            e.setCancelled(false);
            return;
        }

        if (invulnerable.contains(e.getEntity())) {
            e.setCancelled(true);
            return;
        }
        if (invulnerable.contains(resolveDamager(e.getDamager()))) {
            God(resolveDamager(e.getDamager()), false);
            e.setCancelled(true);
            return;
        }

        switch (e.getEntityType()) {
            case PLAYER:
                Player player = (Player) e.getEntity();
                GamePlayer gamePlayer = GamePlayer.getGamePlayer(player);
                if(game.TeamManager().getTeam(resolveDamager(e.getDamager())).equals(gamePlayer.getTeam())){
                    e.setCancelled(true);
                    return;
                }
                if ((player.getHealth() - e.getFinalDamage()) <= 1) {
                    Bukkit.getPluginManager().callEvent(new net.blockcade.Arcade.Managers.EventManager.PlayerDeathEvent(player,e.getCause(),e.getDamager()));
                    if(!game.hasModule(GameModule.DEATH_MANAGER))return;
                    Player killer = resolveDamager(e.getDamager());
                    if (game.TeamManager().getTeam(killer).equals(game.TeamManager().getTeam(player))) {
                        return;
                    }
                    killer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER,0.5f, 0.5f);
                    player.setHealth(20);
                    e.setCancelled(true);
                    doDeath(gamePlayer, String.format(messages[0], player.getDisplayName(), resolveDamager(e.getDamager()).getDisplayName()), e.getDamager());
                }
                break;
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        /*
         * Actual Code
         */
        if(e.getEntity().getType().equals(ARROW))e.setCancelled(true);
        if(game.hasModule(MAX_DAMAGE_HEIGHT)&&e.getEntity().getLocation().getY()>=game.getMaxDamageHeight()){e.setCancelled(true);return;}
        if(game.hasModule(NO_FALL_DAMAGE)&&e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){e.setCancelled(true);return;}
        if(!game.hasModule(GameModule.DEATH_MANAGER))return;
        if(e.getEntityType().equals(EntityType.SILVERFISH))return;
        if (e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) {
            e.setCancelled(true);
            return;
        }
        if (!(game.GameState().equals(GameState.IN_GAME))) return;
        switch (e.getEntityType()) {
            case PLAYER:
                if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))return;
                Player player = (Player) e.getEntity();
                GamePlayer gamePlayer = GamePlayer.getGamePlayer(player);
                if (Spectator.isSpectator(player)) {
                    e.setCancelled(true);
                    return;
                }
                if ((player.getHealth() - e.getFinalDamage()) < 1) {
                    e.setCancelled(true);
                    player.setHealth(20);
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID) || e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                        Random rand = new Random();
                        doDeath(gamePlayer, String.format(fall[rand.nextInt(fall.length)], player.getDisplayName(), gamePlayer.getCombatPlayer().getName()), null);
                    } else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                        Random rand = new Random();
                        doDeath(gamePlayer, String.format(entity_explode[rand.nextInt(entity_explode.length)], player.getDisplayName(), e.getCause()), null);
                    } else {
                        Random rand = new Random();
                        doDeath(gamePlayer, String.format(messages[rand.nextInt(messages.length)], player.getDisplayName(), e.getCause()), null);
                    }
                }
                break;
            default:
                e.setCancelled(true);
        }
    }

    @EventHandler
    public static void doDeath(GamePlayer gamePlayer, String message, Entity damager) {
        Player player = gamePlayer.getPlayer();
        if(!game.hasModule(GameModule.DEATH_MANAGER))return;
        TeamColors team = game.TeamManager().getTeam(player);

        Bukkit.broadcastMessage(Text.format(message));
        player.setHealth(20);
        player.teleport(game.map().getSpawnLocation());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER,0.5f, 0.5f);
        player.setAllowFlight(true);
        player.setFlying(true);
        for(PotionEffect effect:player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        if (game.TeamManager().getCanRespawn(game.TeamManager().getTeam(player))) {
            Spectator.makeSpectator(player, game);
            ItemStack[] armor=player.getInventory().getArmorContents().clone();
            ItemStack[] inventoryContent = player.getInventory().getContents().clone();
            player.getInventory().clear();
            new BukkitRunnable() {
                int timer = Spectator.getSpectatorTime()*2;

                @Override
                public void run() {
                    Text.sendMessage(player, "&cYou died", Text.MessageType.TITLE);
                    Text.sendMessage(player, "Respawning in " + (timer / 2), Text.MessageType.SUBTITLE);
                    timer--;

                    if (timer <= 0) {
                        cancel();
                        Text.sendMessage(player, "", Text.MessageType.TITLE);
                        Text.sendMessage(player, "", Text.MessageType.SUBTITLE);
                        Spectator.removeSpectator(player, game);
                        Text.sendMessage(player, " ", Text.MessageType.TITLE);

                        player.setHealth(20);
                        player.setLevel(0);
                        player.setVelocity(new Vector(0, 0, 0));
                        if(game.GameState().equals(GameState.IN_GAME)){player.setGameMode(GameMode.SURVIVAL);}else{player.setGameMode(GameMode.ADVENTURE);}
                        player.setAllowFlight(false);
                        player.setFlying(false);

                        God(player, true);
                        player.getInventory().setArmorContents(armor);

                        Bukkit.getPluginManager().callEvent(new PlayerRespawnEvent(player,(Player)(damager instanceof Player ?damager:null), false,inventoryContent));
                        new BukkitRunnable() {
                            int invulnerable = 8;

                            @Override
                            public void run() {
                                if (!God(player)) {
                                    Text.sendMessage(player, "&aYou are no longer invulnerable", Text.MessageType.ACTION_BAR);
                                    cancel();
                                    return;
                                }
                                Text.sendMessage(player, String.format("&aInvulnerable for %s second%s", invulnerable / 2, (invulnerable == 1 ? "" : "s")), Text.MessageType.ACTION_BAR);
                                invulnerable--;
                                if (invulnerable <= 1) {
                                    God(player, false);
                                }
                            }
                        }.runTaskTimer(game.handler(), 0L, 10L);
                    }
                }
            }.runTaskTimer(game.handler(), 0L, 10L);
        } else {
            player.getInventory().clear();
            if(damager instanceof Player){
                GamePlayer.getGamePlayer((Player)damager).addElimination();
            }
            Text.sendMessage(player, "&cEliminated", Text.MessageType.TITLE);
            Text.sendMessage(player,"&fYou may no longer respawn", Text.MessageType.SUBTITLE);
            game.TeamManager().doEliminatePlayer(team, player);
            Spectator.makeSpectator(player, game);
        }
    }

    private static boolean God(Entity e) {
        return invulnerable.contains(e);
    }

    private static void God(Entity e, boolean god) {
        if (god) {
            invulnerable.add(e);
        } else {
            invulnerable.remove(e);
        }
    }

    public static Player resolveDamager(Entity e) {
        switch (e.getType()) {
            case ARROW:
                return ((Player) ((Arrow) e).getShooter());
            case PLAYER:
                return (Player) e;
            case SNOWBALL:
                return ((Player) ((Snowball) e).getShooter());
            case PRIMED_TNT:
                return ((Player) ((TNTPrimed) e).getSource());
            default:
                return null;
        }
    }
}

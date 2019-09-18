/*
 *
 *  *
 *  * © Stelch Software 2019, distribution is strictly prohibited
 *  * Blockcade is a company of Stelch Software
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  @since 18/8/2019
 */

/*
 *
 *  *
 *  * © Stelch Software 2019, distribution is strictly prohibited
 *  * Blockcade is a company of Stelch Software
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  @since 22/7/2019
 */

/*
 *
 * *
 *  *
 *  * © Stelch Games 2019, distribution is strictly prohibited
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  * @since 16/7/2019
 *
 */

package net.blockcade.Arcade.Events;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.EventManager.PlayerRespawnEvent;
import net.blockcade.Arcade.Utils.Spectator;
import net.blockcade.Arcade.Utils.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class playerDeathEvent implements Listener {

    Game game;

    public playerDeathEvent(Game game) {
        this.game = game;
    }

    private static ArrayList<Entity> invulnerable = new ArrayList<>();

    private String[] messages = new String[]{
            "%s&7 has been slain by %s",
            "%s&7 has been clapped by %s",
            "%s&7 has been wasted by %s",
            "%s&7 has been rekt by %s"
    };

    private String[] fall = new String[]{
            "%s&7 fell and hit their head",
            "&7Balance is not %s&7's strong point",
            "%s&7 didn't notice the edge",
            "%s&7 was given swift end by the ground"
    };

    private String[] entity_explode = new String[]{
            "%s&7's trusty tnt blew %s&7 to smithereens",
            "&7Boom! &s&7 has blown up %s&7!"
    };

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        e.setDroppedExp(0);
    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent e) {
        if (!(game.GameState().equals(GameState.IN_GAME))) return;

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
                if ((player.getHealth() - e.getFinalDamage()) <= 1) {
                    Player killer = resolveDamager(e.getDamager());
                    if (game.TeamManager().getTeam(killer).equals(game.TeamManager().getTeam(player))) {
                        return;
                    }
                    killer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER,0.5f, 0.5f);
                    player.setHealth(20);
                    e.setCancelled(true);
                    doDeath(player, String.format(messages[0], player.getDisplayName(), resolveDamager(e.getDamager()).getDisplayName()), e.getDamager());
                }
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) {
            e.setCancelled(true);
        }
        if (!(game.GameState().equals(GameState.IN_GAME))) return;
        switch (e.getEntityType()) {
            case PLAYER:
                if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                    return;
                }
                Player player = (Player) e.getEntity();
                if (Spectator.isSpectator(player)) {
                    e.setCancelled(true);
                    return;
                }
                if ((player.getHealth() - e.getFinalDamage()) < 1) {
                    e.setCancelled(true);
                    player.setHealth(20);
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID) || e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                        Random rand = new Random();
                        doDeath(player, String.format(fall[rand.nextInt(fall.length)], player.getDisplayName(), e.getCause()), null);
                    } else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                        Random rand = new Random();
                        doDeath(player, String.format(entity_explode[rand.nextInt(entity_explode.length)], player.getDisplayName(), e.getCause()), null);
                    } else {
                        Random rand = new Random();
                        doDeath(player, String.format(messages[rand.nextInt(messages.length)], player.getDisplayName(), e.getCause()), null);
                    }
                }
                break;
            default:
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void doDeath(Player player, String message, Entity damager) {
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
            int slot = 0;
            for (ItemStack is : player.getInventory().getContents()) {
                slot++;
                if (slot > 99) return;
                if (is == null) continue;
                is.setType(Material.AIR);
            }
            new BukkitRunnable() {
                int timer = 10;

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
                        FileConfiguration config = game.handler().getConfig();
                        Double x = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.x"), "world", team)));
                        Double y = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.y"), "world", team)));
                        Double z = Double.parseDouble(config.getString(String.format(("maps.%s.spawn.%s.z"), "world", team)));
                        Float yaw = Float.parseFloat(config.getString(String.format(("maps.%s.spawn.%s.yaw"), "world", team)));
                        Float pitch = Float.parseFloat("0.0");
                        Location teamSpawn = new Location(player.getWorld(), x, y, z, yaw, pitch);

                        player.setHealth(20);
                        player.setLevel(0);
                        player.setVelocity(new Vector(0, 0, 0));
                        player.teleport(teamSpawn);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setAllowFlight(false);
                        player.setFlying(false);

                        Bukkit.getPluginManager().callEvent(new PlayerRespawnEvent(player, false));

                        God(player, true);
                        new BukkitRunnable() {
                            int invulnerable = 10;

                            @Override
                            public void run() {
                                if (!God(player)) {
                                    Text.sendMessage(player, "&aYou are no longer invulnerable", Text.MessageType.ACTION_BAR);
                                    cancel();
                                    return;
                                }
                                player.getInventory().setArmorContents(armor);
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
            Text.sendMessage(player, "&cEliminated", Text.MessageType.TITLE);
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

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

package net.blockcade.Arcade.Utils;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Spectator implements Listener {

    private static List<Player> spectators = new ArrayList<>();
    private static HashMap<Player, TeamColors> spectator_teams = new HashMap<>();

    public static List<Player> getSpectators() {
        return spectators;
    }

    public static void initializeSpectating() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getSpectators())
                    Text.sendMessage(player, "&aYou are currently Spectating", Text.MessageType.ACTION_BAR);
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
    }

    public static void makeSpectator(Player player, Game game) {
        if (isSpectator(player)) return;
        spectators.add(player);

        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000 * 10, 1));

        for (Player p : Bukkit.getOnlinePlayers())
            p.hidePlayer(player);
    }

    public static void removeSpectator(Player player, Game game) {
        if (!isSpectator(player)) return;
        spectators.remove(player);

        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);

        for (Player p : Bukkit.getOnlinePlayers())
            p.showPlayer(player);
    }

    public static boolean isSpectator(Player player) {
        return getSpectators().contains(player);
    }

    @EventHandler
    public void onSpecDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isSpectator(player))
                event.setCancelled(true);
        }

        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (isSpectator(player))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpecPickup(PlayerPickupItemEvent event) {
        if (isSpectator(event.getPlayer()))
            event.setCancelled(true);
    }

}

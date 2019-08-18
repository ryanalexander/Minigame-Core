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
 *  @since 23/7/2019
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
 *  * @since 14/7/2019
 *
 */

package net.blockcade.Arcade.Managers.GameManagers;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Utils.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class stop {

    public stop(Game game, boolean natural, boolean doStop) {
        game.GameState(GameState.RESETTING);
        if (natural) {
            doFinishGame(game, doStop);
        } else {
            cleanUp(game, doStop);
        }
    }

    public void cleanUp(Game game, boolean stop) {

        game.BlockManager().doRollback();
        if (stop) new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        }.runTaskLater(game.handler(), 20L);
    }

    public void doFinishGame(Game game, boolean stop) {
        TeamColors winner = game.TeamManager().getActive_teams().get(0);
        Bukkit.broadcastMessage(Text.format(String.format("&eCongratulations to %s&e team! You won!", game.TeamManager().getTeamColor(winner) + winner)));
        for (HashMap.Entry<Player, TeamColors> ent : game.TeamManager().getPlayers().entrySet()) {
            if (ent.getValue().equals(winner)) {
                //Text.sendMessage(ent.getKey(),"&6VICTORY", Text.MessageType.TITLE);
                Location loc = ent.getKey().getLocation();
                Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.setPower(2);
                fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

                fw.setFireworkMeta(fwm);
                fw.detonate();

                for (int i = 0; i < 24; i++) {
                    Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                    fw2.setFireworkMeta(fwm);
                }
            } else {
                Text.sendMessage(ent.getKey(), "&cYou Lose", Text.MessageType.TITLE);
                Text.sendMessage(ent.getKey(), "&aBetter luck next time", Text.MessageType.SUBTITLE);
            }
        }

        if (stop) new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Text.format("&aGame> &7The GameCommand has finished."));
                    player.kickPlayer("[GAMESTATE] The GameCommand has finished");
                }
                game.stop(false);
            }
        }.runTaskLater(Main.getPlugin(Main.class), 200);
    }

}

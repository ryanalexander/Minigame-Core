
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

package net.blockcade.Arcade.Managers.GameManagers;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Managers.EventManager.GameEndEvent;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.GameType;
import net.blockcade.Arcade.Varables.Lang.lang;
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
        }.runTaskLater(game.handler(), 60L);
    }

    public void doFinishGame(Game game, boolean stop) {
        Bukkit.getPluginManager().callEvent(new GameEndEvent());
        if (game.GameType().equals(GameType.ELIMINATE)||game.GameType().equals(GameType.DESTROY)) {
            TeamColors winner = game.TeamManager().getActive_teams().get(0);
            Bukkit.broadcastMessage(Text.format(String.format("&eCongratulations to %s&e team! You won!", game.TeamManager().getTeamColor(winner) + winner)));
            for (HashMap.Entry<Player, TeamColors> ent : game.TeamManager().getPlayers().entrySet()) {
                GamePlayer player = GamePlayer.getGamePlayer(ent.getKey());
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        Main.getSqlConnection().query(String.format("UPDATE `player_statistics` SET `wins` = '%s', `losses` = '%s', `kills` = '%s', `final_kills` = '%s', `deaths` = '%s' WHERE `player_uuid`='%s';",
                                player.getCORE_wins()+(winner.equals(game.TeamManager().getTeam(player.getPlayer()))?1:0),
                                player.getCORE_losses()+(winner.equals(game.TeamManager().getTeam(player.getPlayer()))?0:1),
                                player.getCORE_kills(),
                                player.getCORE_final_kills(),
                                player.getCORE_deaths(),
                                player.getPlayer().getUniqueId()
                        ),true);
                    }
                }.runTaskAsynchronously(game.handler());
                if (ent.getValue().equals(winner)) {
                    Text.sendMessage(ent.getKey(),"&6VICTORY", Text.MessageType.TITLE);
                    Text.sendMessage(ent.getKey(), "&aYou are the WINNER!", Text.MessageType.SUBTITLE);
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
        }
        if(game.GameType().equals(GameType.CUSTOM)){
            for(Player p : Bukkit.getOnlinePlayers()) {
                GamePlayer player = GamePlayer.getGamePlayer(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Main.getSqlConnection().query(String.format("UPDATE `player_statistics` SET `wins` = '%s', `losses` = '%s', `kills` = '%s', `final_kills` = '%s', `deaths` = '%s' WHERE `player_uuid`='%s';",
                                player.getCORE_wins(),
                                player.getCORE_losses(),
                                player.getCORE_kills(),
                                player.getCORE_final_kills(),
                                player.getCORE_deaths(),
                                player.getPlayer().getUniqueId()
                        ), true);
                    }
                }.runTaskAsynchronously(game.handler());
            }
        }
        if (stop) new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Text.format(lang.GAME_FINISHED.get()));
                    player.kickPlayer("[GAMESTATE] The game has finished");
                }
                game.stop(false);
            }
        }.runTaskLater(Main.getPlugin(Main.class), 200);
    }

}

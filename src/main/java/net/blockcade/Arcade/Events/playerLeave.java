
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
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class playerLeave implements Listener {

    Game game;

    public playerLeave(Game game) {
        this.game = game;
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        if (game.TeamManager().hasTeam(e.getPlayer()) && !game.TeamManager().isEliminated(game.TeamManager().getTeam(e.getPlayer()))) {
            if(Bukkit.getServer().getOnlinePlayers().size()<=4){
                ArrayList<TeamColors> teams = new ArrayList<>();
                for(Player player : Bukkit.getServer().getOnlinePlayers()){
                    TeamColors team = game.TeamManager().getTeam(player);
                    teams.add(team);
                }
                if(teams.size()<=1){
                    // End game as only 1 player is online
                    game.stop(true);
                }
            }
            new BukkitRunnable() {
                int i = 0;
                TeamColors team = game.TeamManager().getTeam(e.getPlayer());
                @Override
                public void run() {
                    if(e.getPlayer().isOnline())cancel();
                    i++;
                    if (i >= 180) {
                        TeamColors team = game.TeamManager().getTeam(e.getPlayer());
                        for (Player p : game.TeamManager().getTeamPlayers(team)){
                            Text.sendAll(Text.format("&c%s has been eliminated and received a loss stat for this game.",p.getName()), Text.MessageType.TEXT_CHAT);
                        }
                        game.TeamManager().doEliminatePlayer(team, e.getPlayer());
                    }else if (i % 30 == 0) {
                        for (Player p : game.TeamManager().getTeamPlayers(team)){
                            Text.sendAll(Text.format("&c%s has disconnected, waiting %s more minutes before eliminating.",p.getName(),Math.round(i/60)+""), Text.MessageType.TEXT_CHAT);
                        }
                    }
                }
            }.runTaskLater(Main.getPlugin(Main.class), 20L);
        }
    }

}

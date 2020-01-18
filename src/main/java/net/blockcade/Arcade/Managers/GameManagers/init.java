
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
import net.blockcade.Arcade.Managers.EventManager.GameStartEvent;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.JavaUtils;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.Map;

public class init {

    public init(Game game) {
        game.GameState(GameState.IN_GAME);

        if(game.hasModule(GameModule.TEAMS))game.TeamManager().assignTeams();

        Text.sendAll("&aThe game has started", Text.MessageType.ACTION_BAR);

        Bukkit.broadcastMessage(Text.format("&d&m&l============================="));
        Bukkit.broadcastMessage(Text.format(JavaUtils.center(game.title(), 42 + (4))));
        for(String s : game.getGameName().getDescription().split("[\n]")){
            Bukkit.broadcastMessage(Text.format(JavaUtils.center("&e&l"+s, 41 + (4))));
        }
        Bukkit.broadcastMessage(Text.format("&d&m&l============================="));

        for (Map.Entry<Player, TeamColors> payload : game.TeamManager().getPlayers().entrySet()) {
            Player player = payload.getKey();
            player.setGameMode(GameMode.ADVENTURE);
            player.setVelocity(new Vector(0,0,0));
            player.getInventory().setArmorContents(game.TeamManager().getArmor(game.TeamManager().getTeam(player)));
            player.setVelocity(new Vector(0,0,0));
            player.setLevel(0);
            player.getEnderChest().clear();
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setFlying(false);
            player.setAllowFlight(false);
            for(PotionEffect effect:player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            for (Player p : Bukkit.getOnlinePlayers())
                p.showPlayer(game.handler(),player);
            player.setGameMode(GameMode.SURVIVAL);
        }

        Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent());

    }

}

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

package net.blockcade.Arcade.Events;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Utils.Spectator;
import net.blockcade.Arcade.Utils.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.blockcade.Arcade.Varables.GameModule.CHAT_MANAGER;
import static net.blockcade.Arcade.Varables.GameModule.TEAMS;

public class AsyncChatEvent implements Listener {

    Game game;

    public AsyncChatEvent(Game game) {
        this.game = game;
    }

    @EventHandler
    public void ChatEvent(AsyncPlayerChatEvent e) {
        if(!game.hasModule(CHAT_MANAGER))return;
        e.setCancelled(true);
        if (game.GameState() == GameState.IN_GAME) {
            if (Spectator.isSpectator(e.getPlayer())) {
                for (Player p : Spectator.getSpectators()) {
                    Text.sendMessage(p, String.format("&7SPEC | &f%s&7: %s", e.getPlayer().getDisplayName(), e.getMessage()), Text.MessageType.TEXT_CHAT);
                }
                return;
            }
            if(game.hasModule(TEAMS)){
                TeamColors team = game.TeamManager().getTeam(e.getPlayer());
                Bukkit.broadcastMessage(Text.format(String.format("&7%s&7 | &e%s&7: %s", TeamColors.valueOf(team.toString().toUpperCase()).getChatColor() + team.toString().toUpperCase(), e.getPlayer().getName(), e.getMessage())));
            }else {
                Bukkit.broadcastMessage(Text.format(String.format("&e%s&7: %s", e.getPlayer().getName(), e.getMessage())));
            }
        } else {
            Bukkit.broadcastMessage(Text.format(String.format("&e%s&7: %s", e.getPlayer().getDisplayName(), e.getMessage())));
        }
    }

}

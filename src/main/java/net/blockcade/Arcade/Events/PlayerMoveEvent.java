
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
import net.blockcade.Arcade.Managers.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;

import java.util.Objects;
import java.util.Random;

import static net.blockcade.Arcade.Varables.GameModule.VOID_DEATH;

public class PlayerMoveEvent implements Listener {

    Game game;

    public PlayerMoveEvent(Game game) {
        this.game = game;
    }

    @EventHandler
    public void SwimToggle(EntityToggleSwimEvent e){
        if(!e.isSwimming())
            e.setCancelled(true);
    }

    @EventHandler
    public void onMove(org.bukkit.event.player.PlayerMoveEvent e) {
        if(!game.hasModule(VOID_DEATH)){return;}

        if(Objects.requireNonNull(e.getTo()).getY()<=1){
            GamePlayer player = GamePlayer.getGamePlayer(e.getPlayer());
            Random rand = new Random();
            if(player.getCombatPlayer()==null||player.getCombatTime()<=5000){
                playerDeathEvent.doDeath(player, String.format(playerDeathEvent.fall_self[rand.nextInt(playerDeathEvent.fall_self.length)], e.getPlayer().getDisplayName()), null);
                return;
            }
            playerDeathEvent.doDeath(player, String.format(playerDeathEvent.fall[rand.nextInt(playerDeathEvent.fall.length)], e.getPlayer().getDisplayName(), player.getCombatPlayer().getTeam().getChatColor()+" "+player.getCombatPlayer().getName()), null);
        }
    }
}

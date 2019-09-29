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
import net.blockcade.Arcade.Managers.EventManager.PlayerDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class PlayerMoveEvent implements Listener {

    Game game;

    public PlayerMoveEvent(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onMove(org.bukkit.event.player.PlayerMoveEvent e) {
        if(Objects.requireNonNull(e.getTo()).getY()<=1){
            playerDeathEvent.doDeath(e.getPlayer(),"No message",null);
        }
        /*
        if (Main.GameCommand.invis_players.containsKey(e.getPlayer())) {
            Main.GameCommand.invis_players.get(e.getPlayer()).teleport(e.getPlayer());
            Location loc = e.getPlayer().getLocation();
            e.getTo().getWorld().spawnParticle(Particle.DRAGON_BREATH, new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), Float.valueOf(loc.getYaw() + ""), Float.valueOf(loc.getPitch() + "")), 1, 0, 0, 0, 0);
        }*/
    }
}

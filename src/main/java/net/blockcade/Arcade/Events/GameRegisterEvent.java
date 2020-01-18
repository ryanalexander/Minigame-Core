
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameRegisterEvent implements Listener {

    @EventHandler
    public void GameRegisterEvent(net.blockcade.Arcade.Managers.EventManager.GameRegisterEvent e) {

        Game game = e.getGame();

        System.out.println(String.format("[API] The game %s has been registered, hooked correctly.", game.GameType()));

    }
}

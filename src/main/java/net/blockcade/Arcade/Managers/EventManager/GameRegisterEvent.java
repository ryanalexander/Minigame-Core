
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

package net.blockcade.Arcade.Managers.EventManager;

import net.blockcade.Arcade.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameRegisterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Game game;

    public GameRegisterEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
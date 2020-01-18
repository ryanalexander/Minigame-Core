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

import net.blockcade.Arcade.Managers.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCombatLogEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private GamePlayer player;
    private GamePlayer killer;

    public PlayerCombatLogEvent(GamePlayer player) {
        this.player=player;killer=player.getCombatPlayer();
    }

    public GamePlayer getKiller() {
        return killer;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

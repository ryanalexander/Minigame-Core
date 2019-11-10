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

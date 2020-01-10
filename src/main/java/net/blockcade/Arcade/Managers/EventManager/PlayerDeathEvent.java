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

package net.blockcade.Arcade.Managers.EventManager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;


public class PlayerDeathEvent extends Event implements Listener {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private EntityDamageEvent.DamageCause cause;
    private Entity killer;
    private boolean cancelled = false;

    public PlayerDeathEvent(Player player, EntityDamageEvent.DamageCause cause, Entity killer) {
        this.killer = killer;
        this.player = player;
        this.cause = cause;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public Player getPlayer() {
        return player;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCause(EntityDamageEvent.DamageCause cause) {
        this.cause = cause;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Entity getKiller() {
        return killer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

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

import net.blockcade.Arcade.Varables.DeathCause;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static org.bukkit.entity.EntityType.PLAYER;


public class PlayerDeathEvent extends Event implements Listener {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private DeathCause cause;
    private Entity killer;
    private boolean cancelled = false;

    @EventHandler
    public void PlayerDeathEvent(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        DeathCause c;
        switch (e.getCause()) {
            case FALL:
                c = DeathCause.FALL;
            case ENTITY_ATTACK:
                return;
            default:
                c = DeathCause.OTHER;
        }
        Bukkit.getPluginManager().callEvent(new PlayerDeathEvent((Player) e.getEntity(), c, null));
    }

    @EventHandler
    public void PlayerDeathEntityEvent(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        DeathCause c;
        Entity killer;
        switch (e.getDamager().getType()) {
            case PLAYER:
                c = DeathCause.PLAYER;
            case ARROW:
                Arrow travel = (Arrow) e.getEntity();
                if ((travel.getShooter() instanceof Player)) {
                    if (((Player) travel.getShooter()).getType() == PLAYER)
                        c = DeathCause.PLAYER;
                    killer = (Entity) travel.getShooter();
                }
            default:
                c = DeathCause.OTHER;
                killer = e.getDamager();
        }
        Bukkit.getPluginManager().callEvent(new PlayerDeathEvent((Player) e.getEntity(), DeathCause.PLAYER, killer));
    }

    public PlayerDeathEvent(Player player, DeathCause cause, Entity killer) {
        this.killer = killer;
        this.player = player;
        this.cause = cause;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public DeathCause getCause() {
        return cause;
    }

    public Player getPlayer() {
        return player;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCause(DeathCause cause) {
        this.cause = cause;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}

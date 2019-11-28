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
 *  @since 25/7/2019
 */

package net.blockcade.Arcade.Managers.EventManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerRespawnEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Player killer;
    private boolean eliminated;
    private ItemStack[] inventoryContents;

    public PlayerRespawnEvent(Player player, boolean eliminated, ItemStack[] inventoryContents) {
        this.player = player;
        this.eliminated = eliminated;
        this.inventoryContents = inventoryContents;
    }

    public PlayerRespawnEvent(Player player, Player killer, boolean eliminated, ItemStack[] inventoryContents) {
        this.player = player;
        this.killer=killer;
        this.eliminated = eliminated;
        this.inventoryContents = inventoryContents;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getKiller() {
        return killer;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
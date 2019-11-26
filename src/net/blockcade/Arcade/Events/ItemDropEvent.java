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
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropEvent implements Listener {

    Game game;

    public ItemDropEvent(Game game) {
        this.game = game;
    }

    @EventHandler
    public void ItemDropEvent(PlayerDropItemEvent e) {
        if(!game.hasModule(GameModule.NO_TOOL_DROP))return;
        if (game.GameState().equals(GameState.IN_GAME)) {
            if(e.getItemDrop().getItemStack().getType().equals(Material.WOODEN_SWORD)||
            e.getItemDrop().getItemStack().getType().name().toUpperCase().contains("AXE")||
            e.getItemDrop().getItemStack().getType().name().toUpperCase().contains("SHOVEL")||
            e.getItemDrop().getItemStack().getType().name().toUpperCase().contains("SHEARS")
            )
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void ItemMerge(ItemMergeEvent e){
        e.setCancelled(true);
    }

}

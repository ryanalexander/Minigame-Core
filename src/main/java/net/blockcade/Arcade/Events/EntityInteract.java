
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
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.*;

import static net.blockcade.Arcade.Varables.GameModule.NO_HUNGER;
import static net.blockcade.Arcade.Varables.GameModule.NO_SMELTING;

public class EntityInteract implements Listener {

    Game game;

    public EntityInteract(Game game) {
        this.game = game;
    }

    /*
     * Soul consume event?
     */
    @EventHandler
    public void itemPickup(PlayerPickupItemEvent e) {
        if (Spectator.isSpectator(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void InventorySwitch(PlayerItemHeldEvent e) {

        /* TODO Fix invisibility potion
        if(GameCommand.invis_players.containsKey(e.getPlayer())){
            GameCommand.invis_players.get(e.getPlayer()).setItemInHand(e.getPlayer().getInventory().getItem(e.getNewSlot()));
        }
         */

    }

    @EventHandler
    public void EntityDamage(EntityDamageEvent e) {
        if ((e.getEntity() instanceof Player) && Spectator.isSpectator((Player) e.getEntity())) e.setCancelled(true);
        if (!(game.GameState().equals(GameState.IN_GAME))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSmelt(FurnaceSmeltEvent e) {if(!game.hasModule(NO_SMELTING)){return;}if (!(game.GameState().equals(GameState.IN_GAME)))e.setCancelled(true);
    }

    @EventHandler
    public void EntityDamageEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || Spectator.isSpectator((Player) e.getEntity())) e.setCancelled(true);
    }

    @EventHandler
    public void food(FoodLevelChangeEvent e) {
        if(!game.hasModule(NO_HUNGER)){return;}e.setCancelled(true);
    }

    @EventHandler
    public void EntityInteract(PlayerInteractEvent e) {
        if (Spectator.isSpectator(e.getPlayer())) e.setCancelled(true);
        if(game.hasModule(GameModule.CHEST_BLOCK)){
        }
    }

    @EventHandler
    public void ArmorStandEvent(PlayerArmorStandManipulateEvent e){
        if(game.EntityManager().GameEntity(e.getRightClicked()))e.setCancelled(true);
        if (game.EntityManager().hasFunction(e.getRightClicked())) {
            e.setCancelled(true);
            game.EntityManager().EntityFunction(e.getRightClicked()).run(e.getPlayer());
        }
    }

    @EventHandler
    public void EntityInteractEvent(PlayerInteractEntityEvent e) {
        if(e.getRightClicked().getType().equals(EntityType.SILVERFISH)){e.setCancelled(false);return;}
        if (Spectator.isSpectator(e.getPlayer())) e.setCancelled(true);
        if(game.EntityManager().GameEntity(e.getRightClicked()))e.setCancelled(true);
        if (game.EntityManager().hasFunction(e.getRightClicked())) {
            e.setCancelled(true);
            game.EntityManager().EntityFunction(e.getRightClicked()).run(e.getPlayer());
        }
        if (e.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) e.setCancelled(true);
    }

}

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

package net.blockcade.Arcade.Events;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.Lang.lang;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Objects;

import static net.blockcade.Arcade.Varables.GameModule.*;

public class blockPlace implements Listener {

    Game game;

    public blockPlace(Game game) {
        this.game = game;
    }

    @EventHandler
    public void BlockFromToEvent(BlockFromToEvent e) {
        if(!game.hasModule(BLOCK_PLACEMENT)){e.setCancelled(true);return;}
        if(!game.hasModule(BLOCK_ROLLBACK)) return;
        if (game.BlockManager().blocklog.contains(e.getBlock().getLocation())) return;
        if (e.isCancelled()) return;
        if (game.GameState() == GameState.IN_GAME)
            game.BlockManager().update(e.getBlock().getLocation(), e.getBlock().getType(), e.getBlock().getBlockData());
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        if(!game.hasModule(BLOCK_PLACEMENT)){e.setCancelled(true);return;}
        if(!game.hasModule(BLOCK_ROLLBACK))return;
        if (game.BlockManager().blocklog.contains(e.getBlock().getLocation())) {
            return;
        }
        if (e.isCancelled()) return;
        if (Spectator.isSpectator(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        if (game.GameState() == GameState.IN_GAME) {
            if (!game.BlockManager().canPlaceBlock(e.getPlayer(), e.getBlockPlaced().getLocation())) {
                Text.sendMessage(e.getPlayer(), "&cYou may not place a block here.", Text.MessageType.TEXT_CHAT);
                e.setCancelled(true);
                return;
            }

            game.BlockManager().update(e.getBlock().getLocation(), Material.AIR, null);

            if (e.getBlock().getType() == Material.TNT) {
                e.getBlock().setType(Material.AIR);
                TNTPrimed tnt = (TNTPrimed) Objects.requireNonNull(e.getBlock().getLocation().getWorld()).spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
                tnt.setVelocity(new Vector(0,0,0));
                tnt.setFuseTicks(43);
            }
        }
    }

    @EventHandler
    public void craft(CraftItemEvent e) {
        if(!game.hasModule(NO_CRAFTING))return;
        if (e.isCancelled()) return;
        if (game.GameState() == GameState.IN_GAME)
            e.setCancelled(true);
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if(!game.hasModule(BLOCK_PLACEMENT)){e.setCancelled(true);return;}
        if(!game.hasModule(BLOCK_ROLLBACK))return;
        for (Location bl : game.BlockManager().blocklog) {
            if (e.getBlock().getX() == bl.getBlock().getX() && e.getBlock().getY() == bl.getBlock().getY() && e.getBlock().getZ() == bl.getBlock().getZ())
                return;
        }
        if (Spectator.isSpectator(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        if (game.GameState() == GameState.IN_GAME) {
            if (e.isCancelled()) return;
            if (!(game.BlockManager().canBreakBlock(e.getBlock().getLocation()))) {
                e.getPlayer().sendMessage(Text.format(lang.BLOCK_NOT_BREAKABLE.get()));
                e.setCancelled(true);
            }
            //GameCommand.BlockManager().update(e.getBlock().getLocation(),e.getBlock().getType(),e.getBlock().getBlockData());
        }
    }

    @EventHandler
    public void tntExplode(EntityExplodeEvent e) {
        //if(!game.hasModule(BLOCK_ROLLBACK))return;
        if (e.getEntity() instanceof Fireball)
            ((Fireball) e.getEntity()).setIsIncendiary(false);
        e.setCancelled(true);
        for (Block b : e.blockList()) {
            if (game.BlockManager().canBreakBlock(b.getLocation())) {
                game.BlockManager().update(b.getLocation(), b.getType(), b.getBlockData());
                b.setType(Material.AIR);
                Objects.requireNonNull(b.getLocation().getWorld()).spawnParticle(Particle.BLOCK_CRACK,b.getLocation(),4);
            }
        }
        // Don't remove blocks
        e.blockList().clear();
    }


    private static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

}

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

package net.blockcade.Arcade.Managers;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Varables.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockManager {
    private Game game;

    public BlockManager(Game game) {
        this.game = game;
    }


    public ArrayList<Location> blocklog = new ArrayList<>();
    private HashMap<Location, Material> changes = new HashMap<>();
    private HashMap<Location, BlockData> changes_data = new HashMap<>();

    /**
     * Block Handler
     */
    public BlockManager() {
    }

    /**
     * Can player break specific block?
     *
     * @param loc specific block location in world
     */
    public boolean canBreakBlock(Location loc) {
        return this.changes.containsKey(loc) || this.changes_data.containsKey(loc);
    }

    /**
     * @param location Block location
     * @param block    clone of original block
     * @param data     BlockData object
     */
    public void update(Location location, Material block, BlockData data) {
        if (this.changes.containsKey(location)) return;
        this.changes.put(location, block);
        this.changes_data.put(location, data);
    }

    public boolean canPlaceBlock(Player player, Location location) {
        boolean canplace = true;
        if (game.GameState() != GameState.IN_GAME) {
            return false;
        }
        if (game.handler().getConfig().getInt(String.format("maps.%s.block-height", game.map().getName())) <= location.getY()) {
            return false;
        }
        return canplace;
    }

    public void doRollback() {
        Bukkit.broadcastMessage("Starting rollback");
        for (Map.Entry<Location, Material> entry : changes.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
            if (changes_data.containsKey(entry.getKey()) && changes_data.get(entry.getKey()) != null)
                entry.getKey().getBlock().setBlockData(this.changes_data.get(entry.getKey()));
        }
    }

    public static boolean inRegion(Block block, Location location1, Location location2) {
        return blocksFromTwoPoints(location1, location2).contains(block);
    }

    private static List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }
}

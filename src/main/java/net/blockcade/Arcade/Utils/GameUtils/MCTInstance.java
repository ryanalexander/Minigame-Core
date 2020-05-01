
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

package net.blockcade.Arcade.Utils.GameUtils;

import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MCTInstance {

    private TeamColors team = null;
    private Inventory inv;
    private ItemStack[] inv_cache;
    private Player player;
    private Main plugin;

    public MCTInstance(Player player, Inventory inv) {
        this.player = player;
        this.inv_cache = inv.getContents();
        this.inv = inv;
        this.plugin = Main.getPlugin(Main.class);

        buildMenu();
    }

    private void buildMenu() {
        inv.clear();
        if (this.team == null) {
            Item cancel = new Item(Material.BARRIER, "&cLeave MCT");
            cancel.setOnClick(p -> MCTInstance.this.leave());
            for (TeamColors color : TeamColors.values()) {
                Item item = new Item(Material.valueOf(color.getTranslation().toUpperCase() + "_WOOL"), color.getChatColor() + color.name());
                item.setOnClick(player -> {
                    player.sendMessage(Text.format("&aMCT> &7Team color has been set to " + color.getChatColor() + color.name()));
                    MCTInstance.this.team = color;

                    MCTInstance.this.buildMenu();
                });
                this.inv.addItem(item.spigot());
            }
        } else {
            Item spawn = new Item(Material.BEACON, String.format(this.team.getChatColor() + "SET %s SPAWN", this.team));
            Item bed = new Item(Material.LEGACY_BED, String.format(this.team.getChatColor() + "SET %s BED", this.team));
            Item shop = new Item(Material.VILLAGER_SPAWN_EGG, String.format(this.team.getChatColor() + "SET %s SHOP", this.team));
            Item tshop = new Item(Material.CREEPER_SPAWN_EGG, String.format(this.team.getChatColor() + "SET TEAM %s SHOP", this.team));
            Item forge = new Item(Material.GOLD_INGOT, String.format(this.team.getChatColor() + "SET %s FORGE", this.team));

            Item cancel = new Item(Material.BARRIER, "&cReturn to Team selector");

            spawn.setOnClick(p -> {
                p.sendMessage(Text.format(String.format("&aMCT> &7You have set %s&7's SPAWN location.", MCTInstance.this.team.getChatColor() + MCTInstance.this.team.name())));
                setLocation("spawn", p.getLocation());
            });
            bed.setOnClick(p -> {
                p.sendMessage(Text.format(String.format("&aMCT> &7You have set %s&7's BED location.", MCTInstance.this.team.getChatColor() + MCTInstance.this.team.name())));
                setLocation("bed", p.getLocation());
            });
            tshop.setOnClick(p -> {
                p.sendMessage(Text.format(String.format("&aMCT> &7You have set %s&7's Team SHOP location.", MCTInstance.this.team.getChatColor() + MCTInstance.this.team.name())));
                setLocation("team_shop", p.getLocation());
            });
            shop.setOnClick(p -> {
                p.sendMessage(Text.format(String.format("&aMCT> &7You have set %s&7's SHOP location.", MCTInstance.this.team.getChatColor() + MCTInstance.this.team.name())));
                setLocation("shop", p.getLocation());
            });
            forge.setOnClick(p -> {
                p.sendMessage(Text.format(String.format("&aMCT> &7You have set %s&7's FORGE location.", MCTInstance.this.team.getChatColor() + MCTInstance.this.team.name())));
                setLocation("forge", p.getLocation());
            });

            cancel.setOnClick(p -> {
                MCTInstance.this.team = null;
                buildMenu();
            });

            this.inv.addItem(spawn.spigot());
            this.inv.addItem(bed.spigot());
            this.inv.addItem(shop.spigot());
            this.inv.addItem(tshop.spigot());
            this.inv.addItem(forge.spigot());
            this.inv.setItem(8, cancel.spigot());


        }
    }

    private void setLocation(String field, Location loc) {
        plugin.getConfig().set(String.format("maps.%s.%s.%s.x", loc.getWorld().getName(), field, team.name().toUpperCase()), Double.parseDouble("" + loc.getX()));
        plugin.getConfig().set(String.format("maps.%s.%s.%s.y", loc.getWorld().getName(), field, team.name().toUpperCase()), Double.parseDouble("" + loc.getY()));
        plugin.getConfig().set(String.format("maps.%s.%s.%s.z", loc.getWorld().getName(), field, team.name().toUpperCase()), Double.parseDouble("" + loc.getZ()));
        plugin.getConfig().set(String.format("maps.%s.%s.%s.yaw", loc.getWorld().getName(), field, team.name().toUpperCase()), Double.parseDouble("" + loc.getYaw()));
        plugin.saveConfig();
    }

    private Inventory getInv() {
        return inv;
    }

    private ItemStack[] getInv_cache() {
        return inv_cache;
    }

    private TeamColors getTeam() {
        return team;
    }

    private void setTeam(TeamColors team) {
        this.team = team;
    }

    private void leave() {
        this.player.getInventory().clear();
        this.player.getInventory().setContents(inv_cache);
    }
}

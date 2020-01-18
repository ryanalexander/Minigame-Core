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

package net.blockcade.Arcade.Utils.Formatting;

import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang.Validate.notNull;

public class Item implements Listener {
    public static HashMap<Item, click> actions = new HashMap<>();
    public static HashMap<Player, Long> player_delays = new HashMap<>();

    //Game GameCommand;
    ItemStack is;
    ItemMeta im;

    public Item() {

    }

    /*public Item(Game GameCommand){
        this.GameCommand=GameCommand;
    }*/

    public Item(ItemStack is, String name) {
        this.is = is;
        im = this.is.getItemMeta();
        im.setDisplayName(Text.format(name));
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

    }
    public Item(Material material, String name) {
        this.is = new ItemStack(material);
        im = this.is.getItemMeta();
        im.setDisplayName(Text.format(name));
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    /**
     * This function is only applicable to Items that color can be changed on
     */
    public Item setLeatherColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) this.im;
        meta.setColor(color);
        this.im = meta;
        return this;
    }

    public void setName(String name) {
        this.im.setDisplayName(Text.format(name));
    }

    public void setMaterial(Material mat) {
        this.is.setType(mat);
    }

    public void setAmount(int amount) {
        is.setAmount(amount);
    }

    public void setEnchanted(boolean enchanted) {
        im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public void setLore(String... lines){
        ArrayList<String> lines2 = new ArrayList<>();
        for (String line : lines) {
            lines2.add(Text.format(line));
        }
        im.setLore(lines2);
    }

    public Item setOnClick(click onClick) {
        if (onClick != null)
            actions.put(this, onClick);
        return this;
    }

    public ItemStack spigot() {
        this.im.setUnbreakable(true);
        this.is.setItemMeta(this.im);
        return this.is;
    }


    public interface click {
        void run(Player param1Player);
    }

    @EventHandler
    public void EntityInteract(PlayerInteractEvent e) {
        if(Spectator.isSpectator(e.getPlayer()))e.setCancelled(true);
        for (Map.Entry<Item, click> is : actions.entrySet()) {
            if (is.getKey().spigot().equals(e.getItem())) {
                e.setCancelled(true);
                if (is.getValue() != null) {
                    is.getValue().run(e.getPlayer());
                    e.setUseInteractedBlock(Event.Result.DENY);
                    e.setUseItemInHand(Event.Result.DENY);
                }
                return;
            }
        }
    }

    public static ItemStack itemWithBase64(ItemStack item, String base64) {
        notNull(item, "item");
        notNull(base64, "base64");

        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(item,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        if(Spectator.isSpectator((Player)e.getWhoClicked()))e.setCancelled(true);
        /**
         * Ignore armor slots
         */
        if(e.getSlotType() == InventoryType.SlotType.ARMOR) {e.setCancelled(true);}

        for (Map.Entry<Item, click> is : actions.entrySet()) {
            if (is.getKey().spigot().equals(e.getCurrentItem())) {
                e.setCancelled(true);
                if (is.getValue() != null)
                    is.getValue().run((Player) e.getWhoClicked());
                return;
            }
        }
    }

    public enum WoolColor {
        WHITE(0, Color.WHITE), ORANGE(1, Color.ORANGE), MAGENTA(2, null), BLUE(3, Color.BLUE), YELLOW(4, Color.YELLOW), GREEN(5, Color.LIME), PINK(6, Color.FUCHSIA), DARK_GREY(7, Color.GRAY), LIGHT_GREY(8, Color.GRAY), CYAN(9, Color.BLUE), PURPLE(10, Color.PURPLE), DARK_BLUE(11, Color.BLUE), BROWN(12, null), DARK_GREEN(13, Color.GREEN), RED(14, Color.RED), BLACK(15, Color.BLACK);
        byte color;
        Color jColor;

        public Color getjColor() {
            return this.jColor;
        }

        public byte getColor() {
            return this.color;
        }

        WoolColor(int color, Color jColor) {
            this.jColor = jColor;
            this.color = (byte) color;
        }
    }

}

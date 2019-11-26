package net.blockcade.Arcade.Varables;

import org.bukkit.ChatColor;
import org.bukkit.Material;

/*
 * This is for public release games, when developing use "testing"
 */
public enum GameName {
    BEDBATTLES(Material.RED_BED,"Bed Battles","&fFight to defend your bed\nand be the last player standing.","BBW", ChatColor.RED,16),
    CAPTURE(Material.BLUE_BANNER, "Capture The Flag","Capture the enemy team's\nflag three times to win.", "CTF",ChatColor.AQUA,16),
    ARENA(Material.GOLDEN_SWORD, "Arena","Free for all PvP\nwith upgrades & abilities", "AR",ChatColor.RED,100),
    HUB(Material.CLOCK,"HUB","","HUB",ChatColor.WHITE,25);
    Material material;
    String name;
    String description;
    String type;
    ChatColor color;
    int MaxPlayers;
    GameName(Material material, String name, String description, String type, ChatColor color, int MaxPlayers){
        this.material=material;
        this.name=name;
        this.description=description;
        this.type=type;
        this.color=color;
        this.MaxPlayers=MaxPlayers;
    }

    public int getMaxPlayers() {
        return MaxPlayers;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public ChatColor getColor() {
        return color;
    }
}

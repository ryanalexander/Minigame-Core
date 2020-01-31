
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

package net.blockcade.Arcade.Commands;

import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.GameUtils.MCTInstance;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.Ranks;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MCTCommand implements CommandExecutor {

    JavaPlugin plugin;

    public MCTCommand(JavaPlugin main) {
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!sender.isOp()&& GamePlayer.getGamePlayer((Player)sender).getRank().getLevel()< Ranks.HELPER.getLevel()) {
            sender.sendMessage(Text.format("&cYou must be %s&c or higher to execute that command.",Ranks.HELPER.getFormatted()));
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command.");
            return false;
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        if (args.length < 1) {
            sender.sendMessage(new String[]{
                    Text.format("&d----[ &aMap Creator Tools &d]----"),
                    Text.format("&eThis tool was created for Map Creators to"),
                    Text.format("&ehave the ability to specify custom map parameters"),
                    " ",
                    Text.format("&e- &a/mct set &d{spawn/bed/shop/forge/pos1/pos2} {team/id}"),
                    Text.format("&e- &a/mct add &d{emerald/diamond} {mid}"),
                    Text.format("&e- &a/mct remove &d{spawn/forge/blaze/core} {team/id}"),
                    "",
                    Text.format("&e- &a/mct list &d{spawner/blaze/core}")

            });
            return false;
        }

        switch (args[0]) {
            case "set":
                plugin.getConfig().set(String.format("maps.%s.%s.%s.x", loc.getWorld().getName(), args[1].toLowerCase(), args[2].toUpperCase()), Double.parseDouble("" + loc.getX()));
                plugin.getConfig().set(String.format("maps.%s.%s.%s.y", loc.getWorld().getName(), args[1].toLowerCase(), args[2].toUpperCase()), Double.parseDouble("" + loc.getY()));
                plugin.getConfig().set(String.format("maps.%s.%s.%s.z", loc.getWorld().getName(), args[1].toLowerCase(), args[2].toUpperCase()), Double.parseDouble("" + loc.getZ()));
                plugin.getConfig().set(String.format("maps.%s.%s.%s.yaw", loc.getWorld().getName(), args[1].toLowerCase(), args[2].toUpperCase()), Double.parseDouble("" + loc.getYaw()));
                plugin.saveConfig();
                sender.sendMessage(Text.format(String.format("&aMCT> &7Saved &e%s&7 team &e%s&7 location at x:%s y:%s z:%s", args[2].toUpperCase(), args[1], loc.getX(), loc.getY(), loc.getZ())));
                break;
            case "remove":
                sender.sendMessage(Text.format("&aMCT> The &eremove&r command is under construction"));
                sender.sendMessage(Text.format(String.format("&aMCT> &7Removed &e%s&7 location at x:%s y:%s z:%s", args[1], loc.getX(), loc.getY(), loc.getZ())));
                break;
            case "add":
                String key = String.format("maps.%s.%s", ((Player) sender).getLocation().getWorld().getName(), args[1].toUpperCase());
                List<String> list = plugin.getConfig().getStringList(key);
                list.add(String.format("%s:%s:%s:%s", loc.getWorld().getName(), Double.parseDouble("" + loc.getX()), Double.parseDouble("" + loc.getY()), Double.parseDouble("" + loc.getZ())));
                plugin.getConfig().set(key, list);
                sender.sendMessage(Text.format(String.format("&aMCT> &7Saved &e%s&7 location at x:&e%s&7 y:&e%s&7 z:&e%s&7 key: &e%s", args[1].toUpperCase(), Double.parseDouble("" + loc.getX()), Double.parseDouble("" + loc.getY()), Double.parseDouble("" + loc.getZ()), key)));
                plugin.saveConfig();
                break;
            case "blockregion":
                String key_blockregion = String.format("maps.%s.%s", ((Player) sender).getLocation().getWorld().getName(), args[1].toUpperCase());
                Location location = ((Player)sender).getLocation();
                Material block = Material.valueOf(args[2].toUpperCase());
                List<Location> locations = new ArrayList<>();
                int radius = Integer.parseInt(args[3]);
                List<Block> blocks = new ArrayList<>();
                for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++)
                    for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++)
                        for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++)
                            blocks.add(location.getWorld().getBlockAt(x, y, z));
                for(Block b : blocks)
                    if(b.getType().equals(block))
                        locations.add(b.getLocation());
                plugin.getConfig().set(key_blockregion,locations);
                plugin.saveConfig();
                break;
            case "list":
                sender.sendMessage(Text.format("&aMCT> The &elist&r command is under construction"));
                break;
            case "toggle":
                sender.sendMessage(Text.format("&aMCT> &7Configurer now active"));
                new MCTInstance(player, player.getInventory());
                break;
            default:
                sender.sendMessage(Text.format("&aMCT> &fThe command specified does not exist."));
        }
        return false;
    }
}

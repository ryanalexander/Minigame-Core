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
 *  @since 23/7/2019
 */

package net.blockcade.Arcade.Commands;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Utils.Text;
import net.blockcade.Arcade.Varables.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GameCommand implements CommandExecutor {

    JavaPlugin plugin;
    Game game = null;

    public GameCommand(JavaPlugin main, Game game) {
        plugin = main;
        this.game = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command.");
            return false;
        }
        if (!sender.isOp()) {
            sender.sendMessage(Text.format("&cNo Permission"));
            return false;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            sender.sendMessage(new String[]{
                    Text.format("&d----[ &cAdmin &d]----"),
                    " ",
                    Text.format("&e- &a/game &d{start/stop/state}"),

            });
            return false;
        }
        switch (args[0]) {
            case "stop":
                if (args.length >= 2 && args[1].contains("-fs")) {
                    game.stop(false, false);
                    return false;
                }
                if (args.length >= 2 && args[1].contains("-f")) {
                    game.stop(false);
                    return false;
                }
                if (args.length >= 2 && args[1].contains("-s")) {
                    game.stop(true, false);
                    return false;
                }
                if (game.GameState() == GameState.IN_GAME) {
                    game.stop(true);
                } else {
                    player.sendMessage(Text.format("&aADMIN> &7There is no current game in progress."));
                }
                break;
            case "start":
                if (args.length >= 2 && args[1].contains("-f")) {
                    game.init();
                    return false;
                }
                if (game.GameState() == GameState.IN_LOBBY) {
                    game.start();
                } else {
                    player.sendMessage(Text.format("&aADMIN> &7There is already a game in progress."));
                }
                break;
            case "cleanup":

            case "state":
                player.sendMessage(Text.format(String.format("&aADMIN> &7The current GameState is \"&e%s&7\".", game.GameState())));
                break;
            default:
                sender.sendMessage(Text.format("&aADMIN> &fThe command specified does not exist."));
        }
        return false;
    }
}

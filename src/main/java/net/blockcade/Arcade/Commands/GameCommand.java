
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

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.Ranks;
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
        if (!sender.isOp()&&GamePlayer.getGamePlayer((Player)sender).getRank().getLevel()< Ranks.HELPER.getLevel()) {
            sender.sendMessage(Text.format("&cYou must be %s&c or higher to execute that command.",Ranks.HELPER.getFormatted()));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(new String[]{
                    Text.format("&d----[ &cAdmin &d]----"),
                    " ",
                    Text.format("&e- &a/game &d{start/stop/state/debug}"),
                    Text.format("&e- &a/mct &dhelp"),

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
                    sender.sendMessage(Text.format("&aADMIN> &7There is no current game in progress."));
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
                    sender.sendMessage(Text.format("&aADMIN> &7There is already a game in progress."));
                }
                break;
            case "stats":
                GamePlayer player = GamePlayer.getGamePlayer((Player)sender);
                Text.sendMessage(player.getPlayer(),"KILLS: "+player.getKills(), Text.MessageType.TEXT_CHAT);
                Text.sendMessage(player.getPlayer(),"DEATHS: "+player.getDeaths(), Text.MessageType.TEXT_CHAT);
                Text.sendMessage(player.getPlayer(),"ELIMINATIONS: "+player.getEliminations(), Text.MessageType.TEXT_CHAT);
                break;
            case "state":
                sender.sendMessage(Text.format(String.format("&aADMIN> &7The current GameState is \"&e%s&7\".", game.GameState())));
                break;
            default:
                sender.sendMessage(Text.format("&aADMIN> &fThe command specified does not exist."));
        }
        return false;
    }
}

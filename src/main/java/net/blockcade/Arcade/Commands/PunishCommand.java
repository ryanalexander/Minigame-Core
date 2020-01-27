
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
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.Ranks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        // Ensure command is executed by a player
        if(!(sender instanceof Player))return false;

        GamePlayer player = GamePlayer.getGamePlayer((Player)sender);

        // Check perms
        if (!sender.isOp()&&player.getRank().getLevel()< Ranks.HELPER.getLevel()) {
            sender.sendMessage(Text.format("&cYou must be %s&c or higher to execute that command.",Ranks.HELPER.getFormatted()));
            return false;
        }

        return true;
    }
}

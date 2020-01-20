
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
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.GameType;
import net.blockcade.Arcade.Varables.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class debug implements CommandExecutor {

    // TODO Remove this entire file

    Game game;
    public debug(Game game){this.game=game;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!sender.isOp()&& GamePlayer.getGamePlayer((Player)sender).getRank().getLevel()<Ranks.ADMIN.getLevel()) {
            sender.sendMessage(Text.format("&cYou must be %s&c or higher to execute that command.",Ranks.ADMIN.getFormatted()));
            return false;
        }

        // Server Data
        UUID server_uuid = Main.networking.getUuid();
        String container = Main.networking.getContainer();
        String serverName = Main.networking.serverName;
        long UPTIME = ((System.currentTimeMillis()-Main.LAST_START_TIME)/1000);

        // Player Data
        Player player = null;
        GamePlayer gamePlayer = null;
        UUID player_uuid=null;
        Ranks rank=null;
        if(sender instanceof Player) {
            gamePlayer = GamePlayer.getGamePlayer((Player)sender);
            player=gamePlayer.getPlayer();
            player_uuid=gamePlayer.getUuid();
            rank=gamePlayer.getRank();
        }

        // Game Data
        String Name = game.getGameName().getName();
        String Title = game.title();
        boolean Auto_start = game.AutoStart();
        boolean CanStart = game.canStart();
        GameState State = game.GameState();
        GameType Type = game.GameType();
        String Map = game.map().getName();
        int Teams = game.TeamManager().getActive_teams().size();
        int Damage_height = game.getMaxDamageHeight();
        int GameEntities = game.EntityManager().GameEntities().size();
        int FunctionEntities = game.EntityManager().FunctionEntities().size();
        int ManagedBlocks = game.getModules().size();

        sender.sendMessage(Text.format("&e&n______________________"));
        sender.sendMessage(Text.format("&r"));
        sender.sendMessage(Text.format("&f-----&e[SERVER]&f-----"));
        sender.sendMessage(Text.format("&fUUID: &e"+server_uuid));
        sender.sendMessage(Text.format("&fSERVERNAME: &e"+serverName));
        sender.sendMessage(Text.format("&fCONTAINER: &e"+container));
        sender.sendMessage(Text.format("&fUPTIME: &e"+UPTIME+"s"));
        if(gamePlayer!=null) {
            sender.sendMessage(Text.format("&f-----&e[PLAYER]&f-----"));
            sender.sendMessage(Text.format("&fUUID: &e" + player_uuid));
            sender.sendMessage(Text.format("&fRANK: &e" + rank.name()));
        }
        sender.sendMessage(Text.format("&f-----&e[GAME]&f-----"));
        sender.sendMessage(Text.format("&fNAME: &e"+Name));
        sender.sendMessage(Text.format("&fTITLE: &e"+Title));
        sender.sendMessage(Text.format("&fA-START: &e"+Auto_start));
        sender.sendMessage(Text.format("&fC-START: &e"+CanStart));
        sender.sendMessage(Text.format("&fSTATE: &e"+State));
        sender.sendMessage(Text.format("&fTYPE: &e"+Type));
        sender.sendMessage(Text.format("&fMAP: &e"+Map));
        sender.sendMessage(Text.format("&fTEAMS: &e"+Teams));
        sender.sendMessage(Text.format("&fDAMAGEHEIGHT: &e"+Damage_height));
        sender.sendMessage(Text.format("&fGAMEENTITIES: &e"+GameEntities));
        sender.sendMessage(Text.format("&fFUNCTIONENTITIES: &e"+FunctionEntities));
        sender.sendMessage(Text.format("&fMODULES: ("+ManagedBlocks+"): "+game.getModulesString()));
        sender.sendMessage(Text.format("&e&n______________________"));


        return false;
    }
}

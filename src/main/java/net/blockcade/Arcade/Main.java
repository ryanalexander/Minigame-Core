
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

package net.blockcade.Arcade;

import net.blockcade.Arcade.Commands.DebugCommand;
import net.blockcade.Arcade.Commands.MCTCommand;
import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.SQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static net.blockcade.Arcade.Utils.JavaUtils.fixLadder;

public class Main extends JavaPlugin {

    public static Game game;

    public static Networking networking;
    public static long LAST_START_TIME;
    private static SQL sqlConnection;

    @EventHandler
    public void onEnable() {
        LAST_START_TIME=System.currentTimeMillis();
        saveDefaultConfig();
        reloadConfig();
        sqlConnection=new SQL(getConfig().getString("sql.host"),3306,getConfig().getString("sql.user"),getConfig().getString("sql.pass"),getConfig().getString("sql.database"));
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Item(), this);
        //pm.registerEvents(new NCPHandler(), this);
        getCommand("mct").setExecutor(new MCTCommand(this));
        getCommand("debug").setExecutor(new DebugCommand(null));

        for(Player player : Bukkit.getOnlinePlayers()){
            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(player,null));
        }

        // Fix Box Bounding
        fixLadder();
    }

    @Override
    public void onDisable() {
        sqlConnection.close();
        super.onDisable();
    }

    public static SQL getSqlConnection() {
        return sqlConnection;
    }
}

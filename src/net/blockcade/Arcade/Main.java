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

package net.blockcade.Arcade;

import fr.neatmonster.nocheatplus.NCPAPIProvider;
import net.blockcade.Arcade.Commands.mct;
import net.blockcade.Arcade.Utils.Item;
import net.blockcade.Arcade.Utils.NCPHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Networking networking;

    @EventHandler
    public void onEnable() {
        networking = new Networking(this);
        networking.init();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Item(), this);
        NCPAPIProvider.getNoCheatPlusAPI().getEventRegistry().register(new NCPHandler());
        getCommand("mct").setExecutor(new mct(this));
    }

}

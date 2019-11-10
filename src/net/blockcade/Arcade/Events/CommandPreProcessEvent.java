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

/*
 *
 * *
 *  *
 *  * © Stelch Games 2019, distribution is strictly prohibited
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  * @since 16/7/2019
 *
 */

package net.blockcade.Arcade.Events;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreProcessEvent implements Listener {

    Game game;

    public CommandPreProcessEvent(Game game) {
        this.game = game;
    }

    @EventHandler
    public void CommandPreProcessEvent(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/rl") || e
                .getMessage().startsWith("/reload") || e
                .getMessage().startsWith("/stop") && game.GameState() == GameState.IN_GAME) {
            if (!(e.getPlayer() instanceof Player) && e.getPlayer().isOp()) {
                e.getPlayer().sendMessage(Text.format("&cNo Permission"));
                return;
            }

            e.getPlayer().sendMessage(Text.format("&aWarn> &7That command has been delayed by the Mini-Games"));
            e.setCancelled(true);
            game.stop(false);
            Bukkit.getServer().reload();
            e.getPlayer().sendMessage(Text.format("&aServer> &7The server has been reloaded"));
        }
    }
}

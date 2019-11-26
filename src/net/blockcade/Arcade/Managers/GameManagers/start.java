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
 *  * @since 14/7/2019
 *
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
 *  * @since 14/7/2019
 *
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
 *  * @since 14/7/2019
 *
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
 *  * @since 14/7/2019
 *
 */

package net.blockcade.Arcade.Managers.GameManagers;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.Lang.lang;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class start {
    /**
     * @param game instance of Game
     * @see Game instance
     * @since 14/07/2019
     */
    public start(Game game) {
        game.GameState(GameState.STARTING);
        doCountdown(game, 10);
    }

    /**
     * @param game instance of Game
     * @param time (seconds) till GameCommand should start
     * @see Game instance
     * @since 14/07/2019
     */
    public void doCountdown(Game game, int time) {
        // Check GameCommand is in lobby (Don't start countdown if already started)
        if (!(game.GameState().equals(GameState.STARTING))) {
            return;
        }

        // Main counter thread
        new BukkitRunnable() {
            int counter = time;

            @Override
            public void run() {
                if (game.GameState() != GameState.STARTING) {
                    cancel();
                    return;
                }

                String basestr = "";
                for (int i = 0; i < time; i++) {
                    basestr += ((i < counter) ? "&a" : "&c") + "&l&m=";
                }
                Text.sendAll(Text.format("&eStarting in " + basestr + "&r &e" + counter + " second" + ((counter > 1) ? "s" : "")), Text.MessageType.ACTION_BAR);
                if (counter == 10 || counter < 6) {
                    Text.sendAll(String.format(lang.GAME_BEGIN_IN.get(), counter), Text.MessageType.TEXT_CHAT);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                    }
                }
                if (counter <= 1) {
                    game.init();
                    cancel();
                }
                counter--;
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
    }
}

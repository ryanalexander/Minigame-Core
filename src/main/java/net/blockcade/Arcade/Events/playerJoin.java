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
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Managers.ScoreboardManager;
import net.blockcade.Arcade.Utils.Formatting.TABList;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.Lang.lang;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class playerJoin implements Listener {

    Game game;

    public playerJoin(Game game) {
        this.game = game;
    }

    @org.bukkit.event.EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
        e.getPlayer().saveData();
        e.setJoinMessage(null);
        GamePlayer player = GamePlayer.getGamePlayer(e.getPlayer());
        // TODO Scoreboard
        ScoreboardManager sm = new ScoreboardManager(player.getPlayer().getName(),game);
        sm.setGamePlayer(player);
        sm.enableHealthCounter();
        String name = "  BLOCKCADE  ";
        sm.addBlank();
        sm.addLine("&6&lRank");
        sm.addLine(" :RANK:");
        sm.addBlank();
        sm.addLine("&b&lMap");
        sm.addLine(" :map:");
        sm.addBlank();
        sm.addLine("&e&lPlayers");
        sm.addLine(" :player_count: &7/ &f"+game.minPlayers());
        sm.addBlank();
        sm.addLine("&8:server_name: &8- &dblockcade.net");
        sm.addBlank();
        sm.showFor(player.getPlayer());

        TABList.sendPlayerListTab(e.getPlayer(), String.format("&fPlaying &a%s&f on &d&lBLOCKCADE&f!", game.title()), String.format("&fConnected to &a%s", Main.networking.serverName));

        new BukkitRunnable() {
            int offset = 1;
            int expected_offset = name.length();
            boolean left = false;

            @Override
            public void run() {
                if (!(e.getPlayer().isOnline())) {
                    cancel();
                    return;
                }
                if (game.GameState() == GameState.IN_GAME) {
                    cancel();
                    sm.delete();
                    return;
                }
                if (offset <= expected_offset) {
                    left = false;
                    expected_offset = name.length() - 1;
                }
                if (offset >= expected_offset) {
                    left = true;
                    expected_offset = 1;
                }

                String var = "&d&l" +
                        name.substring(0, offset) +
                        "&5&l" + name.substring(offset, offset + 1) + "&f&l" +
                        (offset + 2 <= name.length() ? name.substring(offset + 1) : "");
                sm.setDisplayname(var);
                if (left) {
                    offset--;
                } else {
                    offset++;
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);

        switch (game.GameState()) {
            case IN_LOBBY:
                e.getPlayer().teleport(game.map().getSpawnLocation());
                e.getPlayer().setGameMode(GameMode.ADVENTURE);
                e.getPlayer().getInventory().clear();

                Bukkit.broadcastMessage(Text.format(String.format(lang.GAME_PLAYER_JOIN.get(), e.getPlayer().getDisplayName(), Bukkit.getOnlinePlayers().size(), game.minPlayers())));
                if (game.canStart()) {
                    game.start();
                }
                break;
            case STARTING:
                // Update scoreboard
                Bukkit.broadcastMessage(Text.format(String.format(lang.GAME_PLAYER_JOIN_STARTING.get(), e.getPlayer().getDisplayName(), Bukkit.getOnlinePlayers().size(), game.minPlayers())));
                break;
            case IN_GAME:
                if (game.TeamManager().hasTeam(e.getPlayer())||game.hasModule(GameModule.ALLSTATE_JOIN)) {
                    e.getPlayer().setHealth(20);
                } else {
                    Spectator.makeSpectator(e.getPlayer(), game);
                }
                break;
            default:
                e.getPlayer().kickPlayer(Text.format(String.format("&cERROR> &7Failed to join the requested game. [Game-state: %s]", game.GameState())));
                break;
        }

    }
}

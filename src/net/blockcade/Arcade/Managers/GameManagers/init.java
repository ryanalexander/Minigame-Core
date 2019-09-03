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
import net.blockcade.Arcade.Managers.EventManager.GameStartEvent;
import net.blockcade.Arcade.Utils.Text;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Map;

public class init {

    public init(Game game) {
        game.GameState(GameState.IN_GAME);

        game.TeamManager().assignTeams();

        Text.sendAll("&aThe game has started", Text.MessageType.ACTION_BAR);

        for (Map.Entry<Player, TeamColors> payload : game.TeamManager().getPlayers().entrySet()) {
            Player player = payload.getKey();
            player.setLevel(0);
            player.getEnderChest().clear();
            player.getInventory().clear();
            player.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
            player.getActivePotionEffects().clear();
            player.setFlying(false);
            player.setAllowFlight(false);
            for(PotionEffect effect:player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            for (Player p : Bukkit.getOnlinePlayers())
                p.showPlayer(player);
            player.getInventory().setArmorContents(game.TeamManager().getArmor(game.TeamManager().getTeam(player)));
            player.setGameMode(GameMode.SURVIVAL);
            player.setVelocity(new Vector(0,0,0));
            player.teleport(game.TeamManager().getSpawn(payload.getValue()));
            player.setVelocity(new Vector(0,0,0));
        }
        Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent());

    }

}

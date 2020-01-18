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

package net.blockcade.Arcade.API;

import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Varables.ReportReason;
import org.bukkit.entity.Player;

public class Report {

    GamePlayer gamePlayer;
    Player spigotPlayer;
    ReportReason reason;

    public Report(GamePlayer player, ReportReason reason){
        this.spigotPlayer= player.getPlayer();
        this.gamePlayer=player;
        this.reason=reason;
    }


}

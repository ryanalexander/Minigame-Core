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

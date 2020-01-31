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
 * @since (DD/MM/YYYY) 31/1/2020
 */

package net.blockcade.Arcade.Managers.AdvancementManager.Advancements;

import net.blockcade.Arcade.Managers.AdvancementManager.Achievement;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Varables.RewardType;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class VoidAchievement implements Achievement {


    @Override
    public RewardType getRewardType() {
        return RewardType.EXPERIENCE;
    }

    @Override
    public Integer getRewardCount() {
        return 25;
    }

    @Override
    public boolean hasAchieved(GamePlayer player) {
        return player.getLocation().getY()<=1;
    }
    @Override
    public boolean hasAchieved(GamePlayer player, Block block) {
        return false;
    }
    @Override
    public boolean hasAchieved(GamePlayer player, Entity entity) {
        return false;
    }

    @Override
    public String getTitle() {
        return "Hey Mr Enderman!";
    }

    @Override
    public String getDescription() {
        return "Achieved by falling into the abyss";
    }

    @Override
    public InitiatedBy getInitiator() {
        return InitiatedBy.PLAYER_MOVE;
    }
}

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

package net.blockcade.Arcade.Managers.AdvancementManager;

import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Varables.RewardType;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface Achievement {

    public static enum InitiatedBy {PLAYER_MOVE, ENTITY_ATTACK, BLOCK_BREAK}

    public RewardType getRewardType();
    public Integer getRewardCount();

    public boolean hasAchieved(GamePlayer player);
    public boolean hasAchieved(GamePlayer player, Block block);
    public boolean hasAchieved(GamePlayer player, Entity entity);

    public String getTitle();
    public String getDescription();

    public InitiatedBy getInitiator();
}

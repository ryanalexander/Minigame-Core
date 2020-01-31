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

import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Managers.AdvancementManager.Advancements.VoidAchievement;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class AchievementManager implements Listener {

    private HashMap<Achievement.InitiatedBy, ArrayList<Achievement>> advancements;

    public static enum AdvancementAction {
        BREAK_BLOCK,
        ATTACK_ENEMY
    }

    public AchievementManager() {
        this.advancements=new HashMap<>();
        for(Achievement.InitiatedBy initiatedBy : Achievement.InitiatedBy.values())
            advancements.put(initiatedBy,new ArrayList<>());
        // Register listener
        Bukkit.getPluginManager().registerEvents(this,Main.getPlugin(Main.class));
        registerAdvancement(new VoidAchievement());
    }

    public void registerAdvancement(Achievement achievement){
        Bukkit.getLogger().info("Loaded "+achievement.getTitle()+" achievement.");
        this.advancements.get(achievement.getInitiator()).add(achievement);
    }
    private void doAdvancement(Achievement advancement, GamePlayer player){
        if(advancement.hasAchieved(player)&&!player.hasAchieved(advancement)){
            player.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5f,0.5f);
            TextComponent message = (new TextComponent(Text.format("&d&lChallenge Complete! ")));
            TextComponent title = new TextComponent(advancement.getTitle());
            title.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,TextComponent.fromLegacyText(Text.format(""))));
            message.addExtra(title);
            player.getPlayer().spigot().sendMessage(message);
            player.giveAchievement(advancement);
        }
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        for(Achievement advancement : advancements.get(Achievement.InitiatedBy.PLAYER_MOVE))
            doAdvancement(advancement,GamePlayer.getGamePlayer(e.getPlayer()));
    }
    @EventHandler
    public void BlockBreak(BlockBreakEvent e){
        for(Achievement advancement : advancements.get(Achievement.InitiatedBy.BLOCK_BREAK))
            doAdvancement(advancement,GamePlayer.getGamePlayer(e.getPlayer()));
    }
}

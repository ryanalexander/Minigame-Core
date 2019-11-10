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

package net.blockcade.Arcade.Utils.Formatting;


import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Formatter;

public class Text {
    public enum MessageType {
        TEXT_CHAT,
        TITLE,
        SUBTITLE,
        DRAGON_BAR,
        ACTION_BAR;

        MessageType() {
        }
    }

    public enum NotifyType {
        JOIN,
        LEAVE,
        KICK,
        GAME,
        ANNOUNCE,
        PERMISSION,
        ADMIN;

        NotifyType() {
        }
    }

    public static boolean sendAll(String msg, MessageType type) {
        for (Player player : Bukkit.getOnlinePlayers())
            sendMessage(player, msg, type);
        return true;
    }

    public static boolean sendMessage(Player p, String msg, NotifyType type) {
        return sendMessage(p, "&a" + type + "> &7" + msg, MessageType.TEXT_CHAT);
    }

    public static boolean sendMessage(Player p, String msg, MessageType type) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        switch (type) {
            case TEXT_CHAT:
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg + "&r"));
                return true;
            case TITLE:
                p.sendTitle(format(msg), null, 0, 60, 0);
                return true;
            case SUBTITLE:
                p.sendTitle(null, format(msg), 0, 60, 0);
                return true;
            case DRAGON_BAR:
                return true;
            case ACTION_BAR:
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\"}"), ChatMessageType.GAME_INFO));
                return true;
        }
        return false;
    }

    public static String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', "&r" + str);
    }

    public static String format(String str, String... args) {
        return ChatColor.translateAlternateColorCodes('&', "&r" + (new Formatter().format(str, args)));
    }
}
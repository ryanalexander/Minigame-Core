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
 *  * @since 18/7/2019
 *
 */

package net.blockcade.Arcade.Utils.Formatting;

import net.blockcade.Arcade.Utils.ReflectionUtil;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TABList {
    public static void sendPlayerListTab(Player player, String header, String footer) {
        try {
            CraftPlayer craftplayer = (CraftPlayer) player;
            PlayerConnection connection = craftplayer.getHandle().playerConnection;
            IChatBaseComponent head = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + Text.format(header) + "\"}"));
            IChatBaseComponent foot = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + Text.format(footer) + "\"}"));
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            Field headerField = packet.getClass().getDeclaredField("header");
            headerField.setAccessible(true);
            headerField.set(packet, head);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = packet.getClass().getDeclaredField("footer");
            footerField.setAccessible(true);
            footerField.set(packet, foot);
            footerField.setAccessible(!footerField.isAccessible());

            Method sendPacket = ReflectionUtil.getNMSClass("PlayerConnection").getMethod("sendPacket", ReflectionUtil.getNMSClass("Packet"));
            sendPacket.invoke((PlayerConnection)ReflectionUtil.getConnection(player), packet);
            connection.sendPacket(packet);
        } catch (Exception var10) {
            var10.printStackTrace();
        }
    }
}

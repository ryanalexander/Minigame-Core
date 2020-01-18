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
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class TABList {
    public static void sendPlayerListTab(Player player, String header, String footer) {
        try {
            Class<?> chatSerializer = ReflectionUtil.getNMSClass("IChatBaseComponent$ChatSerializer");

            Class<?> packetPlayerListHeaderFooter = ReflectionUtil.getNMSClass("PacketPlayOutPlayerListHeaderFooter");
            Constructor<?> packetPlayerListHeaderFooterConstructor = packetPlayerListHeaderFooter.getDeclaredConstructor();

            Object tabHeader = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\": \"" + header + "\"}");
            Object tabFooter = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\": \"" +footer + "\"}");

            Object headerPacket = packetPlayerListHeaderFooterConstructor.newInstance(tabHeader);

            Field field = headerPacket.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(headerPacket, tabFooter);

            ReflectionUtil.sendPacket(player,headerPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

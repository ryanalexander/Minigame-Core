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

package net.blockcade.Arcade.Varables;

import org.bukkit.Color;

public enum TeamColors {
    RED("&c", Color.RED, "RED"), BLUE("&9", Color.BLUE, "BLUE"), AQUA("&b", Color.AQUA, "CYAN"), YELLOW("&e", Color.YELLOW, "YELLOW"), WHITE("&f", Color.WHITE, "WHITE"), GRAY("&7", Color.GRAY, "LIGHT_GRAY"), PINK("&d", Color.MAROON, "PINK"), GREEN("&a", Color.LIME, "LIME");
    private Color color;
    private String chatColor;
    private String translation;

    public String getChatColor() {
        return chatColor;
    }

    public Color getColor() {
        return this.color;
    }

    public String getTranslation() {
        return this.translation;
    }

    TeamColors(String chatColor, Color Color, String translation) {
        this.color = Color;
        this.chatColor = chatColor;
        this.translation = translation;
    }
}

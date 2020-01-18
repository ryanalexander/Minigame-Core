
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

package net.blockcade.Arcade.Varables;

public enum Ranks {
        MEMBER(0,"&f&l"),
        SUPER(1,"&a&l"),
        MEGA(2,"&b&l"),
        ULTRA(3,"&d&l"),
        MEDIA(10,"&6&l"),
        HELPER(50, "&9&l"),
        ADMIN(85,"&c&l");
        private int level;
        private String rank;
        public int getLevel() {return this.level;}
        public String getFormatted() {return this.getColor()+this.name()+"&r";}
        public String getColor() {return this.rank;}
        private Ranks(int level, String rank){this.rank = rank.toUpperCase();this.level=level;}
}
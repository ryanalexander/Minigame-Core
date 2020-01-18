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

package net.blockcade.Arcade.Varables.Lang;

public enum lang {

    GAME_PLAYER_JOIN("&a&lJOIN &9%s&7 has joined the lobby (&6%s&7/&6%s&7)."),
    GAME_PLAYER_JOIN_STARTING("&a&lJOIN &9%s&7 has joined the lobby (&6%s&7/&6%s&7)."),

    GAME_BEGIN_IN("&a&lGAME &7The game will begin in &6%s&7 seconds."),
    GAME_TEAM_ASSIGNED("&a&lTEAM &7You have been assigned to &b&l%s&7 team"),

    GAME_STOPPED_ADMIN("&c&lGAME &7Game stopped by an Administrator"),
    GAME_FINISHED("&9&lGAME &7The game has finished. Returned to lobby."),


    BLOCK_NOT_BREAKABLE("&cYou may only break blocks placed by players."),
    CHEST_TEAM_NOT_ELIMINATED("&cYou may not open this chest until &e%s&c team is eliminated.");

    private String message;

    public String get() {
        return this.message;
    }

    lang(String message) {
        this.message = message;
    }
}
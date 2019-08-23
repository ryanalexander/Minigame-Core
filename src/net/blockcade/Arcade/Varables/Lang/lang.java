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
    GAME_PLAYER_JOIN("&aJoin> &7Welcome &9%s&7, ( &9%s &7/ &9%s &7) for game to start"),
    GAME_PLAYER_JOIN_STARTING("&aJoin> &7Welcome &9%s&7, ( &9%s &7/ &9%s &7)"),

    GAME_BEGIN_IN("&aGame> &7The game will begin in &6%s&7 seconds."),
    GAME_TEAM_ASSIGNED("&aGame> &7You have been assigned to &e%s&7 team."),

    GAME_STOPPED_ADMIN("&aGame> &7The game has been &cstopped&7 by an Administrator."),
    GAME_FINISHED("&aGame> &7The game has finished, returning to lobby."),

    GAME_CORE_DESTROYED("%s&c team's core has been destroyed. They can no longer respawn"),

    BLOCK_NOT_BREAKABLE_SABOTAGE("&cHey! That's your core."),
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
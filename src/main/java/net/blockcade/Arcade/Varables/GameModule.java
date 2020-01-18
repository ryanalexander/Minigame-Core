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

public enum GameModule {
    /*
     *   *                     *
     *  *   Module Definitions   *
     *   *                     *
     *
     * START_MECHANISM - Game starting mechanics
     *  - Start Countdown
     *  - Pre-Game Lobby
     *  - Team Assignment (If enabled)
     *
     * CHEST_BLOCK - Allows settings of TeamChests
     *
     * DEATH_MANAGER - Respawn players to their team spawn (Static spawns are not possible, will need to be integrated in your game)
     *
     * NO_TOOL_DROP - Prevent players dropping Swords, Axes, Pickaxes and Shovels
     *
     * BLOCK_PLACEMENT - Allow players to place/break blocks
     *
     * BLOCK_ROLLBACK - Blocks placed by players will be rolled back on game end
     *
     * TEAMS - Team related Mechanics
     *  - Team Assignment
     *  - Coloured Armor
     *  - Per-Team spawning
     *
     * NO_CRAFTING - Prevent players from crafting items
     * NO_SMELTING - Prevent players from smelting items
     * NO_HUNGER - Prevent players from losing hunger
     * NO_WEATHER_CHANGE - Prevent natural weather changing
     *
     * VOID_DEATH - Instantly kill players once they are in the void
     *
     * ALLStATE_JOIN - Allow players to join the game regardless of the state (Note: If state is Disabled they will not be able to join)
     *
     * CHAT_MANAGER - Will the plugin format chat (If disabled, you alternative will need to follow guidelines, check with superior
     *
     * ERROR_CATCHER - This will watch the game and ensure that all progression markers are met as expected. If a major stage fails all players will be returned to the lobby.
     *
     * INFINITE_BUILDING - Enable infinite building
     */
    START_MECHANISM,
    CHEST_BLOCK,
    DEATH_MANAGER,
    NO_TOOL_DROP,
    BLOCK_PLACEMENT,
    BLOCK_ROLLBACK,
    TEAMS,
    NO_CRAFTING,
    NO_SMELTING,
    NO_HUNGER,
    VOID_DEATH,
    ALLSTATE_JOIN,
    CHAT_MANAGER,
    NO_FALL_DAMAGE,
    MAX_DAMAGE_HEIGHT,
    @Deprecated
    BOUNTIES,
    NO_WEATHER_CHANGE,
    ERROR_CATCHER,
    INFINITE_BUILDING
}

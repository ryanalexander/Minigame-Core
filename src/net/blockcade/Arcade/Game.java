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

package net.blockcade.Arcade;

import net.blockcade.Arcade.Events.*;
import net.blockcade.Arcade.Managers.BlockManager;
import net.blockcade.Arcade.Managers.EntityManager;
import net.blockcade.Arcade.Managers.EventManager.GameRegisterEvent;
import net.blockcade.Arcade.Managers.GameManagers.init;
import net.blockcade.Arcade.Managers.GameManagers.start;
import net.blockcade.Arcade.Managers.GameManagers.stop;
import net.blockcade.Arcade.Managers.ScoreboardManager;
import net.blockcade.Arcade.Managers.TeamManager;
import net.blockcade.Arcade.Utils.Spectator;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.GameType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static org.bukkit.entity.EntityType.ITEM_FRAME;
import static org.bukkit.entity.EntityType.PLAYER;

public class Game {

    // Game Title
    private String title;

    // Game Type
    private GameType gameType;

    // TeamManager instance
    private TeamManager teamManager;

    // Minimum players for game to start (Naturally)
    private int min_players;

    // Max players the game will allow
    private int max_players;

    // Handler for game
    private JavaPlugin handler;

    // Block Manager
    private BlockManager blockManager;

    // EntityManager
    private EntityManager entityManager;

    // ScoreboardManager
    private ScoreboardManager scoreboardManager;

    // Default map for game
    private World map;

    // Current gameState
    private GameState gameState;

    // Should the game autostart
    private boolean AutoStart;

    // Game Modules
    private ArrayList<GameModule> gameModules;

    /**
     * @param title       What will the game be called?
     * @param gameType    Game type from gameType enum
     * @param min_players Minimum players for game to start (Naturally)
     * @param max_players Max players the game will allow
     * @param handler     Handler for game
     * @param map         Default map for game
     * @since 14/07/2019
     */
    public Game(String title, GameType gameType, int min_players, int max_players, JavaPlugin handler, World map) {
        this.title = title;
        this.gameType = gameType;
        this.min_players = min_players;
        this.max_players = max_players;
        this.handler = handler;
        this.map = map;
        this.gameModules = new ArrayList<>();
        this.entityManager = new EntityManager(this);
        this.teamManager = new TeamManager(this);
        this.blockManager = new BlockManager(this);
        this.gameState = GameState.DISABLED;
        this.AutoStart = true;
        Main.networking.setGame(title());
        // Remove entities
        for (Entity e : map.getEntities()) {
            if (e.getType() != PLAYER&&e.getType()!= EntityType.ITEM_FRAME) {
                if(e.getType().equals(ITEM_FRAME)){((ItemFrame)e).setItem(null);continue;}
                e.remove();
            }
        }
        handler.getServer().getPluginManager().registerEvents(new ItemDropEvent(this),handler);
        Spectator.initializeSpectating();
        registerEvents();

        // Game registration finished. Now inform all other plugins that game has Registered
        Bukkit.getPluginManager().callEvent(new GameRegisterEvent(this));
    }

    /**
     * @return are to minimum requirements met for game to start
     * @since 14/07/2019
     */
    public boolean canStart() {
        boolean start = true;
        if (Bukkit.getOnlinePlayers().size() < this.min_players) {
            start = false;
        }
        if (!this.AutoStart) {
            start = false;
        }
        return start;
    }

    /**
     * @return Game type from gameType enum
     * @since 14/07/2019
     */
    public GameType GameType() {
        return gameType;
    }

    /**
     * @return Max players the game will allow
     * @since 14/07/2019
     */
    public int maxPlayers() {
        return max_players;
    }

    /**
     * @param max_players Max players the game will allow
     * @since 14/07/2019
     */
    public void maxPlayers(int max_players) {
        this.max_players = max_players;
    }

    /**
     * @return Minimum players for game to start (Naturally)
     * @since 14/07/2019
     */
    public int minPlayers() {
        return min_players;
    }

    /**
     * @param min_players Minimum players for game to start (Naturally)
     * @since 14/07/2019
     */
    public void minPlayers(int min_players) {
        this.min_players = min_players;
    }

    /**
     * @return Handler for game
     * @since 14/07/2019
     */
    public Plugin handler() {
        return handler;
    }

    /**
     * @return What will the game be called?
     * @since 14/07/2019
     */
    public String title() {
        return title;
    }

    /**
     * @param title What will the game be called?
     * @since 14/07/2019
     */
    public void title(String title) {
        this.title = title;
    }

    /**
     * @param gameType Game type from gameType enum
     * @since 14/07/2019
     */
    public void GameType(GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * @param map Set current map for game
     * @since 14/07/2019
     */
    public void map(World map) {
        this.map = map;
    }

    /**
     * @return Default map for game
     * @since 14/07/2019
     */
    public World map() {
        return map;
    }

    /**
     * @return fetch current GameState
     * @since 14/07/2019
     */
    public GameState GameState() {
        return this.gameState;
    }

    /**
     * @param gameState from GameState enum, which gameState shall be set
     * @see GameState for options
     * @since 14/07/2019
     */
    public void GameState(GameState gameState) {
        this.gameState = gameState;
        Main.networking.setGameState(gameState.name());
    }

    /**
     * @return will the game autostart when requirements are met
     * @since 14/07/2019
     */
    public boolean AutoStart() {
        return AutoStart;
    }

    /**
     * @param auto_start should the game autostart when requirements are met
     * @since 14/07/2019
     */
    public void AutoStart(boolean auto_start) {
        this.AutoStart = auto_start;
    }

    /**
     * @return Game instance of TeamManager
     * @since 14/07/2019
     */
    public TeamManager TeamManager() {
        return teamManager;
    }

    /**
     * @return Game instance of ScoreboardManager
     * @since 16/07/2019
     */
    public ScoreboardManager ScoreboardManager() {
        return scoreboardManager;
    }

    /**
     * @return Game instance of EntityManager
     * @since 14/07/2019
     */
    public EntityManager EntityManager() {
        return entityManager;
    }

    /**
     * @return BlockManager instance
     * @since 14/07/2019
     */
    public BlockManager BlockManager() {
        return this.blockManager;
    }

    /**
     *
     * @param module GameModule
     * @return If the game has the specific module enabled
     * @since 23/08/2019
     */
    public boolean hasModule(GameModule module){
        return this.gameModules.contains(module);
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // Main game start function
    public void start() {
        new start(this);
    }

    // Initialize the game
    public void init() {
        new init(this);
    }

    // Main gamer stop function
    public void stop(boolean isNatural) {
        new stop(this, isNatural, true);
    }

    public void stop(boolean isNatural, boolean doStop) {
        new stop(this, isNatural, doStop);
    }

    // Register required events
    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new AsyncChatEvent(this), handler);
        pm.registerEvents(new blockPlace(this), handler);
        pm.registerEvents(new EntityInteract(this), handler);
        pm.registerEvents(new playerDeathEvent(this), handler);
        pm.registerEvents(new playerJoin(this), handler);
        pm.registerEvents(new playerLeave(this), handler);
        pm.registerEvents(new PlayerMoveEvent(this), handler);
    }
}
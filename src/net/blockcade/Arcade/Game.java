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
import net.blockcade.Arcade.Managers.*;
import net.blockcade.Arcade.Managers.EventManager.GameRegisterEvent;
import net.blockcade.Arcade.Managers.GameManagers.init;
import net.blockcade.Arcade.Managers.GameManagers.start;
import net.blockcade.Arcade.Managers.GameManagers.stop;
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
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

    // Max Damage Height
    private int MaxDamageHeight=0;

    /**
     * Initialize Game
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
        updateGamerules();

        // Game registration finished. Now inform all other plugins that game has Registered
        Bukkit.getPluginManager().callEvent(new GameRegisterEvent(this));
    }

    /**
     * Minimum requirements for game to start naturally requires {@link GameModule#START_MECHANISM}
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
     * Get GameType
     * @return Game type from gameType enum
     * @since 14/07/2019
     */
    public GameType GameType() {
        return gameType;
    }

    /**
     * Get Max players the game will allow
     * @return Max players the game will allow
     * @since 14/07/2019
     */
    public int maxPlayers() {
        return max_players;
    }

    /**
     * Set Max players the game will allow
     * @param max_players Max players the game will allow
     * @since 14/07/2019
     */
    public void maxPlayers(int max_players) {
        this.max_players = max_players;
    }

    /**
     * Get Minimum players for game to start (Naturally)
     * @return Minimum players for game to start (Naturally)
     * @since 14/07/2019
     */
    public int minPlayers() {
        return min_players;
    }

    /**
     * Set Minimum players for game to start (Naturally)
     * @param min_players Minimum players for game to start (Naturally)
     * @since 14/07/2019
     */
    public void minPlayers(int min_players) {
        this.min_players = min_players;
    }

    /**
     * JavaPlugin for the Game
     * @return Handler for game
     * @since 14/07/2019
     */
    public Plugin handler() {
        return handler;
    }

    /**
     * Get Game Name
     * @return What will the game be called?
     * @since 14/07/2019
     */
    public String title() {
        return title;
    }

    /**
     * Set Game Name
     * @param title What will the game be called?
     * @since 14/07/2019
     */
    public void title(String title) {
        this.title = title;
    }

    /**
     * Set Game Type {@link GameType}
     * @param gameType Game type from gameType enum
     * @since 14/07/2019
     */
    public void GameType(GameType gameType) {
        this.gameType = gameType;
        updateGamerules();
    }

    /**
     * Set World the game will operate in
     * @param map Set current map for game {@link World}
     * @since 14/07/2019
     */
    public void map(World map) {
        this.map = map;
    }

    /**
     * Set Max Damage Height requires module {@link GameModule#MAX_DAMAGE_HEIGHT}
     * @param maxDamageHeight Max Damage Y Axis
     */
    public void setMaxDamageHeight(int maxDamageHeight) {
        MaxDamageHeight = maxDamageHeight;
    }

    /**
     * Get World the game will operate in
     * @return Default map for game
     * @since 14/07/2019
     */
    public World map() {
        return map;
    }

    /**
     * Get current GameState
     * @return fetch current GameState
     * @since 14/07/2019
     */
    public GameState GameState() {
        return this.gameState;
    }

    /**
     * Set GameState {@link GameState}
     * @param gameState from GameState enum, which gameState shall be set
     * @see GameState for options
     * @since 14/07/2019
     */
    public void GameState(GameState gameState) {
        this.gameState = gameState;
        Main.networking.setGameState(gameState.name());
    }

    /**
     * Get weather the game will start naturally requires {@link GameModule#START_MECHANISM}
     * @return will the game autostart when requirements are met
     * @since 14/07/2019
     */
    public boolean AutoStart() {
        return AutoStart;
    }

    /**
     * Set if the game should start naturally
     * @param auto_start should the game autostart when requirements are met
     * @since 14/07/2019
     */
    public void AutoStart(boolean auto_start) {
        this.AutoStart = auto_start;
    }

    /**
     * Get TeamManager instance {@link TeamManager} requires {@link GameModule#TEAMS}
     * @return Game instance of TeamManager
     * @since 14/07/2019
     */
    public TeamManager TeamManager() {
        return teamManager;
    }

    /**
     * Get ScoreboardManager instance {@link ScoreboardManager}
     * @return Game instance of ScoreboardManager
     * @since 16/07/2019
     */
    public ScoreboardManager ScoreboardManager() {
        return scoreboardManager;
    }

    /**
     * Get EntityManager instance {@link EntityManager}
     * @return Game instance of EntityManager
     * @since 14/07/2019
     */
    public EntityManager EntityManager() {
        return entityManager;
    }

    /**
     * Get BlockManager instance {@link BlockManager} requires {@link GameModule#BLOCK_PLACEMENT} and {@link GameModule#BLOCK_ROLLBACK}
     * @return BlockManager instance
     * @since 14/07/2019
     */
    public BlockManager BlockManager() {
        return this.blockManager;
    }

    /**
     * Set Max Damage Height for the server requires {@link GameModule#MAX_DAMAGE_HEIGHT}
     * @return Current MaxDamageHeight on Y Axis
     */
    public int getMaxDamageHeight() {
        return MaxDamageHeight;
    }

    /**
     * Check if {@link GameModule} is enabled
     * @param module GameModule
     * @return If the game has the specific module enabled
     * @since 23/08/2019
     */
    public boolean hasModule(GameModule module){
        return this.gameModules.contains(module);
    }

    public void setModule(GameModule module, boolean state){
        if(state){
            if(!this.gameModules.contains(module)){
                this.gameModules.add(module);
            }
        }else{
            this.gameModules.remove(module);
        }
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
        pm.registerEvents(new GamePlayer(this), handler);
        pm.registerEvents(new playerJoin(this), handler);
        pm.registerEvents(new playerLeave(this), handler);
        pm.registerEvents(new PlayerMoveEvent(this), handler);
    }

    private void updateGamerules() {
        switch(GameType()){
            case CUSTOM:
                for(GameModule module:GameModule.values()){setModule(module,false);}
            case DESTROY:
                for(GameModule module:GameModule.values()){setModule(module,true);}
                break;
            case CAPTURE:
                setModule(GameModule.DEATH_MANAGER,true);
                setModule(GameModule.TEAMS,true);
                setModule(GameModule.BLOCK_PLACEMENT,true);
                setModule(GameModule.BLOCK_ROLLBACK,true);
                setModule(GameModule.NO_TOOL_DROP,false);
                setModule(GameModule.NO_CRAFTING,true);
                setModule(GameModule.NO_SMELTING,true);
                setModule(GameModule.NO_HUNGER,true);
                setModule(GameModule.CHEST_BLOCK,true);
                setModule(GameModule.VOID_DEATH,true);
                break;
            default:
                for(GameModule module:GameModule.values()){setModule(module,false);}
        }
    }
}
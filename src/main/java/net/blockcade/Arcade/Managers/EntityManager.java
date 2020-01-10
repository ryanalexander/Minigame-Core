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


package net.blockcade.Arcade.Managers;

import net.blockcade.Arcade.Game;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityManager {
    private Game game;

    public EntityManager(Game game) {
        this.game = game;
    }

    private ArrayList<Entity> gameEntities = new ArrayList<>();

    private HashMap<Entity, click> EntityFunctions = new HashMap<>();

    public boolean GameEntity(Entity e) {
        return gameEntities.contains(e);
    }

    public boolean AddGameEntity(Entity e) {
        return gameEntities.contains(e);
    }

    public Entity CreateEntity(Entity e) {
        AddGameEntity(e);
        return e;
    }

    public Entity CreateEntity(Location location, EntityType type, String name) {
        Entity e = location.getWorld().spawnEntity(location, type);
        e.setCustomName(name);
        e.setCustomNameVisible(true);
        AddGameEntity(e);
        return e;
    }

    public void remove(Entity e) {
        if (hasFunction(e)) EntityFunctions.remove(e);
        if (GameEntity(e)) gameEntities.remove(e);
        e.remove();
    }

    public void setFunction(Entity e, click c) {
        EntityFunctions.put(e, c);
    }

    public boolean hasFunction(Entity e) {
        return EntityFunctions.containsKey(e);
    }

    public click EntityFunction(Entity e) {
        return EntityFunctions.get(e);
    }

    public HashMap<Entity, click> FunctionEntities() {
        return EntityFunctions;
    }
    public ArrayList<Entity> GameEntities() {
        return gameEntities;
    }

    public interface click {
        void run(Player param1Player);
    }
}
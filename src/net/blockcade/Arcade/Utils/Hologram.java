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

package net.blockcade.Arcade.Utils;

import net.blockcade.Arcade.Game;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private Game game;

    private String[] lines;

    private Location location;

    private List<ArmorStand> lineEntities;

    /**
     * @param lines New argument for each line
     */
    public Hologram(Game game, Location location, String[] lines) {
        this.game = game;
        this.lines = lines;
        this.lineEntities = new ArrayList<>();
        this.location = location;
    }

    public void editLine(int line, String string) {
        this.lineEntities.get(line).setCustomName(Text.format(string));
    }

    public List<ArmorStand> getLineEntities() {
        return lineEntities;
    }

    public Location getLocation() {
        return location;
    }

    public String[] getLines() {
        return lines;
    }

    public void remove() {
        for (Entity e : getLineEntities())
            game.EntityManager().remove(e);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private void build() {
        for (String line : this.lines) {
            this.lineEntities.add((ArmorStand) game.EntityManager().CreateEntity(location, EntityType.ARMOR_STAND, line));
            Location addX = this.location;
            addX.add(0.25, 0, 0);
            this.location = addX;
        }
    }

}

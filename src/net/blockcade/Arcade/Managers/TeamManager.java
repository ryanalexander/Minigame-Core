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
import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.Lang.lang;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamManager {

    private HashMap<Player, TeamColors> players = new HashMap<>();
    private ArrayList<TeamColors> active_teams = new ArrayList<>();
    private HashMap<TeamColors, ArrayList<Player>> team_players = new HashMap<>();

    private ArrayList<TeamColors> cantRespawn = new ArrayList<>();

    private int maxTeams = 0;

    private HashMap<TeamColors, Integer> scoreboard_lines = new HashMap<>();

    private Game game;

    public TeamManager(Game game) {
        this.game = game;
    }

    public List<Player> getTeamPlayers(TeamColors teamColors) {
        return team_players.get(teamColors);
    }

    public TeamColors getTeam(Player p) {
        return players.get(p);
    }

    public boolean isEliminated(TeamColors team) {
        return (!active_teams.contains(team));
    }

    public boolean hasTeam(Player p) {
        return players.containsKey(p);
    }

    public ArrayList<TeamColors> getActive_teams() {
        return this.active_teams;
    }

    public Location[] getProtectedRegion(TeamColors team) {
        List<String> locations = game.handler().getConfig().getStringList(String.format("maps.%s.protection-zone.%s", game.map().getName(), team.name().toUpperCase()));
        String[] pos1_data = locations.get(0).split("[:]");
        String[] pos2_data = locations.get(1).split("[:]");
        Location pos1 = new Location(game.map(), Integer.parseInt(pos1_data[1]), Integer.parseInt(pos1_data[2]), Integer.parseInt(pos1_data[3]));
        Location pos2 = new Location(game.map(), Integer.parseInt(pos2_data[1]), Integer.parseInt(pos2_data[2]), Integer.parseInt(pos2_data[3]));
        return new Location[]{pos1, pos2};
    }

    public Location getSpawn(TeamColors team) {
        System.out.println("[Debug] Finding spawn "+String.format(("maps.%s.spawn.%s.x"), game.map().getName(), team.name())+"");
        Double x = Double.parseDouble(game.handler().getConfig().getString(String.format(("maps.%s.spawn.%s.x"), game.map().getName(), team.name())));
        Double y = Double.parseDouble(game.handler().getConfig().getString(String.format(("maps.%s.spawn.%s.y"), game.map().getName(), team.name())));
        Double z = Double.parseDouble(game.handler().getConfig().getString(String.format(("maps.%s.spawn.%s.z"), game.map().getName(), team.name())));
        Float yaw = Float.parseFloat(game.handler().getConfig().getString(String.format(("maps.%s.spawn.%s.yaw"), game.map().getName(), team.name())));
        Float pitch = Float.parseFloat("0.0");
        return new Location(game.map(), x, y, z, yaw, pitch);
    }

    public void setCantRespawn(TeamColors team, boolean state) {
        if (state) {
            cantRespawn.add(team);
        } else {
            cantRespawn.remove(team);
        }
    }

    public boolean getCanRespawn(TeamColors team) {
        return !cantRespawn.contains(team);
    }

    public String getTeamColor(TeamColors team) {
        return TeamColors.valueOf(team.toString().toUpperCase()).getChatColor();
    }

    public void doEliminatePlayer(TeamColors team, Player player) {
        this.team_players.get(team).remove(player);
        if (this.getRemainingPlayers(team) <= 0) {
            this.doEliminateTeam(team);
        }
    }

    public int getRemainingPlayers(TeamColors team) {
        return this.team_players.get(team).size();
    }

    public void doEliminateTeam(TeamColors team) {
        Bukkit.broadcastMessage(Text.format(String.format("%s&7 team have been eliminated!", getTeamColor(team) + team)));
        this.active_teams.remove(team);
        if (this.getActive_teams().size() == 1) {
            game.stop(true);
        }
    }

    public ItemStack[] getArmor(TeamColors team) {
        Item BOOTS = new Item(Material.LEATHER_BOOTS, String.format("%s&7 teams BOOTS", team.getChatColor() + team.name())).setLeatherColor(team.getColor());
        Item LEGGINGS = new Item(Material.LEATHER_LEGGINGS, String.format("%s&7 teams LEGGINGS", team.getChatColor() + team.name())).setLeatherColor(team.getColor());
        Item CHESTPLACE = new Item(Material.LEATHER_CHESTPLATE, String.format("%s&7 teams CHESTPLACE", team.getChatColor() + team.name())).setLeatherColor(team.getColor());
        Item HELMET = new Item(Material.LEATHER_HELMET, String.format("%s&7 teams HELMET", team.getChatColor() + team.name())).setLeatherColor(team.getColor());
        return new ItemStack[]{BOOTS.spigot(), LEGGINGS.spigot(), CHESTPLACE.spigot(), HELMET.spigot()};
    }

    public void setScoreboardLine(TeamColors team, int id) {
        this.scoreboard_lines.put(team, id);
    }

    public int getScoreboardLine(TeamColors team) {
        return this.scoreboard_lines.get(team);
    }

    public boolean getConfigBool(String endpoint, TeamColors team) {
        return (game.handler().getConfig().getBoolean(String.format(("maps.%s.%s.%s"), game.map().getName(), endpoint, team)));
    }

    public String getConfigString(String endpoint, TeamColors team) {
        return (game.handler().getConfig().getString(String.format(("maps.%s.%s.%s"), game.map().getName(), endpoint, team)));
    }

    public Integer getConfigInt(String endpoint, TeamColors team) {
        return (game.handler().getConfig().getInt(String.format(("maps.%s.%s.%s"), game.map().getName(), endpoint, team)));
    }

    public Double getConfigDouble(String endpoint, TeamColors team) {
        return (game.handler().getConfig().getDouble(String.format(("maps.%s.%s.%s"), game.map().getName(), endpoint, team)));
    }

    public Long getConfigLong(String endpoint, TeamColors team) {
        return (game.handler().getConfig().getLong(String.format(("maps.%s.%s.%s"), game.map().getName(), endpoint, team)));
    }

    public Location getConfigLocation(String endpoint, TeamColors team) {
        Double x = Double.parseDouble(game.handler().getConfig().getString(String.format(("maps.%s.%s.%s.x"), game.map().getName(), endpoint, team)));
        Double y = Double.parseDouble(game.handler().getConfig().getString(String.format(("maps.%s.%s.%s.y"), game.map().getName(), endpoint, team)));
        Double z = Double.parseDouble(game.handler().getConfig().getString(String.format(("maps.%s.%s.%s.z"), game.map().getName(), endpoint, team)));
        Float yaw = Float.parseFloat(game.handler().getConfig().getString(String.format(("maps.%s.%s.%s.yaw"), game.map().getName(), endpoint, team)));
        return new Location(game.map(), x, y, z, yaw, 0f);
    }

    private void setTeam(Player p, TeamColors t) {
        players.put(p, t);
        p.setDisplayName(Text.format(t.getChatColor() + p.getName()));
        p.setPlayerListName(p.getDisplayName());
        if (!(this.active_teams.contains(t))) {
            this.team_players.put(t, new ArrayList<>());
            this.active_teams.add(t);
        }
        this.team_players.get(t).add(p);
    }

    public void assignTeams() {
        int iterator = 0;
        TeamColors[] teams = TeamColors.values();

        for (Player p : Bukkit.getOnlinePlayers()) {
            TeamColors t;
            if (iterator <= teams.length) {
                t = teams[iterator];
                setTeam(p, t);
                iterator++;
                if(iterator>=(maxTeams))iterator=0;
            } else {
                iterator = 0;
                t = teams[iterator];
                setTeam(p, t);
                iterator++;
            }
            p.sendMessage(Text.format(String.format(lang.GAME_TEAM_ASSIGNED.get(), t.getChatColor() + t.name().toUpperCase())));
        }
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public HashMap<Player, TeamColors> getPlayers() {
        return players;
    }
}

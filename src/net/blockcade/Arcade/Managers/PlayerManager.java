package net.blockcade.Arcade.Managers;

import org.bukkit.entity.Player;

public class PlayerManager {

    private Player player;

    private int level=0;

    private int kills=0;
    private int deaths=0;

    private boolean eliminate=false;

    public PlayerManager(Player player){

    }

    public int getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }

    private void addKill() {this.kills=this.kills+1;}
    private void addDeath() {this.deaths=this.deaths+1;}

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setEliminate(boolean eliminate) {
        this.eliminate = eliminate;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }
}

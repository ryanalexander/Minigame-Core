package net.blockcade.Arcade.Utils;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ErrorCatcher {

    Game game;

    public ErrorCatcher(Game game) {
        this.game=game;
        new BukkitRunnable(){
            GameState state;
            int stage_markers = 0;
            @Override
            public void run() {
                if(!game.hasModule(GameModule.ERROR_CATCHER))return;
                switch(game.GameState()){
                    case IN_LOBBY:
                        if(state!=GameState.IN_LOBBY){stage_markers=0;state=GameState.IN_LOBBY;}
                        if(game.minPlayers()<= Bukkit.getOnlinePlayers().size()&&game.AutoStart()){
                            // Game should be starting
                            System.out.println("[STAGE MARK ERROR] Failed "+state+" stage progression [MARKER:"+stage_markers+"]");
                            state=GameState.IN_LOBBY;
                            stage_markers++;
                            if(stage_markers>=4){
                                System.out.println("[STAGE MARK ERROR] Failed "+state+" fatally. Stopping game");
                                EndGame();
                            }
                        }
                        break;
                    case STARTING:
                        if(state!=GameState.STARTING){stage_markers=0;state=GameState.STARTING;}
                        if(stage_markers>=30){
                            System.out.println("[STAGE MARK ERROR] Failed "+state+" fatally. Stopping game");
                            EndGame();
                        }
                        break;
                    case IN_GAME:
                        if(state!=GameState.IN_GAME){stage_markers=0;state=GameState.IN_GAME;}
                        if(Bukkit.getOnlinePlayers().size()<=1){
                            // No-one left in the server
                            if(stage_markers>=3){
                                game.stop(true,true);
                            }
                        }
                        break;
                }
            }
        }.runTaskTimerAsynchronously(game.handler(),0L,10L);
    }

    public void EndGame() {
        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.broadcastMessage(Text.format("&cAn internal error has occurred. This game has been cancelled."));
                game.stop(false,true);
            }
        }.runTask(game.handler());
    }

}

package net.blockcade.Arcade.Events;

import net.blockcade.Arcade.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import static net.blockcade.Arcade.Varables.GameModule.NO_WEATHER_CHANGE;

public class WeatherChange implements Listener {

    Game game;
    public WeatherChange(Game game){this.game=game;}

    @EventHandler
    public void WeatherChange(WeatherChangeEvent e){
        if(game.hasModule(NO_WEATHER_CHANGE)){
            e.setCancelled(true);
        }
    }

}

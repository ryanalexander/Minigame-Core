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

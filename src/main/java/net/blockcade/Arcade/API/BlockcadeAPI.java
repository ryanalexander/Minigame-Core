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

package net.blockcade.Arcade.API;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Main;

public class BlockcadeAPI {

    ReportManager reportManager;

    public Game getGame() {
        return Main.game;
    }

}

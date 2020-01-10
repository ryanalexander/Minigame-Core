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
 *  @since 23/7/2019
 */

package net.blockcade.Arcade;

import net.blockcade.Arcade.Utils.JedisUtils;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class Networking {

    private UUID uuid = UUID.randomUUID();
    public String serverName="loading";

    private String container="";

    private String gameState = "DISABLED";
    private String game = "";

    private Main plugin;
    private static JedisPool pool;

    public Networking(Main plugin) {
        this.plugin = plugin;
    }

    public void init() {
        if (pool == null) {
            pool = JedisUtils.init();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/etc/hostname"));
            container=reader.readLine();
            reader.close();
        }catch (Exception e){
            container="NULL";
        }

        /**
         * Add to Bungee
         */
        System.out.println("[Redis] Pushing server to network.");

        new BukkitRunnable() {
            @Override
            public void run() {
                try (Jedis jedis = pool.getResource()) {
                    String name = jedis.get(String.format("SERVER|%s|name", uuid));
                    if (name != null) {
                        serverName = name;
                        cancel();
                    } else {
                        System.out.println(uuid+" | Failed to get name. Trying again soon.");
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, (20 * 5));

        new BukkitRunnable() {
            @Override
            public void run() {
                pushData();
            }
        }.runTaskTimer(plugin, 0L, (20 * 5));
    }

    public String getContainer() {
        return container;
    }

    public String getGame() {
        return game;
    }

    public String getGameState() {
        return gameState;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public void setGame(String game) {
        this.game = game;
    }

    private void pushData() {
        try (Jedis jedis = pool.getResource()) {
            if (serverName != null && serverName != "loading" && jedis.get(String.format("SERVER|%s|name", uuid)) == null)
                jedis.set(String.format("SERVER|%s|name", uuid), serverName);
            jedis.set(String.format("SERVER|%s|ipport", uuid), getServer().getIp() + ":" + getServer().getPort());
            jedis.set(String.format("SERVER|%s|type", uuid), plugin.getConfig().getString("server-type"));
            jedis.set(String.format("SERVER|%s|playercount", uuid), getServer().getOnlinePlayers().size() + "");
            jedis.set(String.format("SERVER|%s|game", uuid), this.game);
            jedis.set(String.format("SERVER|%s|state", uuid), this.gameState);
            jedis.set(String.format("SERVER|%s|last_poll", uuid), Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() + "");

            jedis.set(String.format("SERVER|%s|container", uuid), container);
        }
    }
}
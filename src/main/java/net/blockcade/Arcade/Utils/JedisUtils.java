
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

package net.blockcade.Arcade.Utils;

import redis.clients.jedis.JedisPool;

public class JedisUtils {

    public static JedisPool init() {
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        System.out.println("[Redis] Connection began and cached.");
        Thread.currentThread().setContextClassLoader(previous);
        return new JedisPool("mc2.stelch.gg");
    }

    public static void end(redis.clients.jedis.JedisPool pool) {
        pool.destroy();
    }

}

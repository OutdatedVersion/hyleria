package com.hyleria.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/18/2017 (7:31 PM)
 */
public class PlayerUtil
{

    /**
     * @return the online player count
     */
    public static int onlineCount()
    {
        return Bukkit.getServer().getOnlinePlayers().length;
    }

    /**
     * @return a collection of every player who is online
     */
    public static List<? extends Player> everyone()
    {
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers());
    }

    /**
     * @return a stream containing everyone that's online
     */
    public static Stream<? extends Player> everyoneStream()
    {
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers()).stream();
    }

    /**
     * Set's the provided player's health
     * to the maximum value it may be at
     * the time this is invoked.
     *
     * @param player the player
     */
    public static void fullHealth(Player player)
    {
        player.setHealth(player.getMaxHealth());
    }

}

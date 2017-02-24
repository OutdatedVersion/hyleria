package com.hyleria.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/18/2017 (7:31 PM)
 */
public class PlayerUtil
{

    /**
     * @return a collection containing everyone that's online
     */
    public static Stream<Player> everyone()
    {
        return Arrays.asList(Bukkit.getServer().getOnlinePlayers()).stream();
    }

}

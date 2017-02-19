package com.hyleria.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/18/2017 (7:31 PM)
 */
public class PlayerUtil
{

    /**
     * @return a collection containing everyone that's online
     */
    public static Collection<? extends Player> everyone()
    {
        return Bukkit.getOnlinePlayers();
    }

}

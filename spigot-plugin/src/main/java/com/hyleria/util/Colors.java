package com.hyleria.util;

import org.bukkit.ChatColor;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (12:20 AM)
 */
public class Colors
{

    private Colors() { }

    /** the color for player related things */
    public static final ChatColor PLAYER = ChatColor.GREEN;

    /**
     * Turns the provided {@link ChatColor} into
     * the bolded version of itself.
     *
     * @param color the color
     * @return the color, BUT bold
     */
    public static String bold(ChatColor color)
    {
        return color.toString() + ChatColor.BOLD;
    }

}

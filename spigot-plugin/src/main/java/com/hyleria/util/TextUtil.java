package com.hyleria.util;

import org.bukkit.ChatColor;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/20/2017 (12:51 PM)
 */
public class TextUtil
{

    private TextUtil() { }

    /**
     * Formats the provided boolean into
     * a human friendly format
     *
     * @param val the boolean
     * @return formatted text
     */
    public static String enabledDisabled(boolean val)
    {
        return enabledDisabled(val, false);
    }

    /**
     * Formats the provided boolean into
     * a human friendly format
     *
     * @param val the boolean
     * @return formatted text
     */
    public static String enabledDisabledBold(boolean val)
    {
        return enabledDisabled(val, true);
    }

    /**
     * Internal
     *
     * @return a formatted string
     */
    private static String enabledDisabled(boolean val, boolean bold)
    {
        return val ? bold ? Colors.BOLD : "" + (ChatColor.GREEN + "Enabled")
                   : bold ? Colors.BOLD : "" + (ChatColor.RED + "Disabled");
    }

}

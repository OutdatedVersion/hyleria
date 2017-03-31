package com.hyleria.util;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/20/2017 (12:51 PM)
 */
public class TextUtil
{

    private TextUtil() { }

    /** join on spaces */
    public static final Joiner SPACE_JOINER = Joiner.on(" ");

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
        return val ? (bold ? Colors.BOLD : "" + (ChatColor.GREEN + "Enabled"))
                   : (bold ? Colors.BOLD : "" + (ChatColor.RED + "Disabled"));
    }

    /**
     * Returns a {@link String} formatted to
     * English's possessive S rule
     *
     * @param text input
     * @return input formatted to a possessive {@code S}
     */
    public static String s(String text)
    {
        if (text.endsWith("s") || text.endsWith("z"))
            return text + "'";

        return text + "'s";
    }

    /**
     * Turns the provided enum constant
     * into a human-friendly version
     * of itself
     *
     * @param val the enum
     * @return the formatted name
     */
    public static String formatEnum(Enum val)
    {
        return WordUtils.capitalizeFully(val.name().toLowerCase().replaceAll("_", " "));
    }

    /**
     * Turn the provided array into a
     * single String
     *
     * @param array the array
     * @return the string
     */
    public static String arrayToString(String[] array)
    {
        return SPACE_JOINER.join(array);
    }

}

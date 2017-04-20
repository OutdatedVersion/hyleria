package com.hyleria.bungee.util;

import net.md_5.bungee.api.ChatColor;

/**
 * Holds random colors we need to
 * reduce the amount of "boiler plate"
 * contained within the code base.
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/24/2017 (11:49 AM)
 */
public class Colors
{

    private Colors() { }

    /** MAKE IT STRONG */
    public static final String BOLD = ChatColor.BOLD.toString();

    /** keep it golden boys */
    public static final String GOLD_BOLD = ChatColor.GOLD + BOLD;

    /** dark like my life */
    public static final String GRAY_BOLD = ChatColor.GRAY + BOLD;

    /** pretty soft color */
    public static final String DARK_AQUA_BOLD = ChatColor.DARK_AQUA + BOLD;

    /** gettin brighter */
    public static final String AQUA_BOLD = ChatColor.AQUA + BOLD;

}

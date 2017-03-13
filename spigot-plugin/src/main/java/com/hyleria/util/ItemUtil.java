package com.hyleria.util;

import org.bukkit.Material;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/12/2017 (11:54 PM)
 */
public class ItemUtil
{

    public static Material cookedItemFor(Material material)
    {
        switch (material)
        {
            case RAW_BEEF:
                return Material.COOKED_BEEF;

            case RAW_CHICKEN:
                return Material.COOKED_CHICKEN;

            case RAW_FISH:
                return Material.COOKED_FISH;

            default:
                return null;
        }
    }

}

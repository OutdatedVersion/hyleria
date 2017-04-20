package com.hyleria.util;

import org.bukkit.Material;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (7:49 PM)
 */
public class BlockUtil
{

    /**
     * Returns what the provided Block {@link Material}
     * will drop. This does not take "regular" drops into
     * consideration, but only ones deemed "precious"; such
     * as gems/rare metals.
     *
     * @param block the block's composition material
     * @return what this block will drop, or silently fail
     *         with a {@code null} return value
     */
    public static Material preciousDropFromBlock(Material block)
    {
        switch (block)
        {
            case IRON_ORE:
                return Material.IRON_INGOT;

            case COAL_ORE:
                return Material.COAL;

            case GOLD_ORE:
                return Material.GOLD_INGOT;

            case QUARTZ_ORE:
                return Material.QUARTZ;

            // drops item instantly (doesn't require smelting)
            case DIAMOND_ORE:
                return Material.DIAMOND;

            case EMERALD_ORE:
                return Material.EMERALD;

            default:
                throw new IllegalArgumentException("Could not find any dropable match for: " + block.name());
        }
    }

}

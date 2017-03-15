package com.hyleria.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (12:52 AM)
 */
public class VectorUtil
{

    /**
     * @param a loc one
     * @param b loc two
     * @return the offset between the two
     */
    public static double offset(Location a, Location b)
    {
        return offset(a.toVector(), b.toVector());
    }

    /**
     * @param a vector one
     * @param b vector two
     * @return the offset between the two
     */
    public static double offset(Vector a, Vector b)
    {
        return a.subtract(b).length();
    }

}

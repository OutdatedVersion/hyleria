package com.hyleria.util;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
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

    /**
     * @param a location one
     * @param b location two
     * @return the offset
     */
    public static double offset2D(Location a, Location b)
    {
        return offset2D(a.toVector(), b.toVector());
    }

    /**
     * Calculates the offset between two points
     * with zero regard to the elevation of either
     *
     * @param a point one
     * @param b point two
     * @return the offset
     */
    public static double offset2D(Vector a, Vector b)
    {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    /**
     * Turns a WorldEdit vector into
     * a Bukkit vector.
     *
     * @param vector the bukkit vector
     * @return the WE vector
     */
    public static com.sk89q.worldedit.Vector fromBukkit(Vector vector)
    {
        return new com.sk89q.worldedit.Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Checks whether the player (from the
     * provided move event) has actually moved
     * their physical character a decent distance.
     * This is mostly for ignoring events that
     * only contain head movement.
     *
     * @param event the event
     * @return yes or no
     */
    public static boolean hasPlayerMovedLocation(PlayerMoveEvent event)
    {
        return offset2D(event.getFrom(), event.getTo()) <= 0;
    }

}

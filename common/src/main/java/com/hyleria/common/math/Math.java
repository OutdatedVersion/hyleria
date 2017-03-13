package com.hyleria.common.math;

import com.google.common.base.Preconditions;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A set of mathematics related methods
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/12/2017 (12:35 PM)
 */
public class Math
{

    private Math() { }

    /**
     * Returns some arbitrary number who's
     * maximum value is that of the provided
     * {@code bound}. (w/ a minimal value of 0)
     *
     * @param bound the highest this number may be
     * @return the random number
     */
    public static int random(double bound)
    {
        return ThreadLocalRandom.current().nextInt((int) bound);
    }

    /**
     * Returns an arbitrary number with a
     * value that may not exceed that of
     * the bound provided, and will not
     * fall under the "starting point"
     * provided
     *
     * @param startingPoint the minimum value
     * @param bound the maximum value
     * @return the number
     */
    public static int random(double startingPoint, double bound)
    {
        return ThreadLocalRandom.current().nextInt((int) startingPoint, (int) bound);
    }

    /**
     * Checks whether or not the provided
     * number is in the range of the {@code min}
     * and {@code max} points used. Though,
     * it may <strong>not</strong> be one of
     * these bounds.
     *
     * @param num the number
     * @param origin the bottom of the range
     * @param bound the top of the range
     * @return yes or no
     */
    public static boolean betweenExclusive(int num, int origin, int bound)
    {
        return num > origin && num < bound;
    }

    /**
     * Checks whether or not the provided
     * number is above the minimum value,
     * and below the maximum value. This
     * does return {@code true} if the
     * number is one of the bounds as well.
     *
     * @param num the number
     * @param origin the min val
     * @param bound the max val
     * @return yes or no
     */
    public static boolean betweenInclusive(int num, int origin, int bound)
    {
        return num >= origin && num <= bound;
    }

    /**
     * Returns whether or not the percent
     * chance of something happening, happens.
     *
     * @param percent the percent
     * @return yes or no
     */
    public static boolean chance(double percent)
    {
        return random(100) <= percent;
    }

    /**
     * Calculates the size of a Minecraft
     * inventory based on a count of rows.
     *
     * @param rows the row count
     * @return the size
     */
    public static int inventorySizeFromRows(int rows)
    {
        Preconditions.checkState(rows <= 6, "an inventory can not have more than 6 rows");

        return rows * 9;
    }

}

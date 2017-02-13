package net.hyleriamc.commons.util;

/**
 * @author Ben (OutdatedVersion)
 * @since Jan/03/2017 (4:01 PM)
 */
public class TimeUtil
{

    /**
     * Checks whether or not the provided
     * amount of time has elapsed.
     *
     * @param from     the time (in ms) that we're comparing against
     * @param required the time that needs to have elapsed (in ms)
     *
     * @return whether it has, or not
     */
    public static boolean elapsed(long from, long required)
    {
        return System.currentTimeMillis() - from > required;
    }

}

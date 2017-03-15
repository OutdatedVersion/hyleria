package com.hyleria.common.time;

import java.util.concurrent.TimeUnit;

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

    /**
     * Turns the provided text into
     * a Java representation of that
     * time
     *
     * @param text text we're looking in
     * @return the unit
     */
    public static TimeUnit textToUnit(String text)
    {
        switch (text.toLowerCase())
        {
            case "ns":
                return TimeUnit.NANOSECONDS;

            case "mc":
                return TimeUnit.MICROSECONDS;

            case "ms":
                return TimeUnit.MILLISECONDS;

            case "s":
            case "sec":
                return TimeUnit.SECONDS;

            case "m":
            case "min":
            case "mins":
                return TimeUnit.MINUTES;

            case "h":
            case "hr":
            case "hrs":
                return TimeUnit.HOURS;

            default:
                throw new RuntimeException("No unit match found for: " + text);
        }
    }

}

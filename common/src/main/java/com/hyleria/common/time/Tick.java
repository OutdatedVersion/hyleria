package com.hyleria.common.time;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (12:01 AM)
 */
public enum Tick
{

    SECONDS(1),
    MINUTES(60),
    HOURS(3600);

    /** the amount of seconds in this value */
    private final int seconds;

    Tick(int seconds)
    {
        this.seconds = seconds;
    }

    /**
     * @return this in {@code ticks}
     */
    public long toTicks()
    {
        return seconds * 20;
    }

    /**
     * @param quantity the amount of these units
     *                 that we have
     * @return this in {@code ticks}
     */
    public long toTicks(int quantity)
    {
        return quantity * toTicks();
    }

}

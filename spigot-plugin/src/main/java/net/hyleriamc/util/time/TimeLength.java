package net.hyleriamc.util.time;

import net.hyleriamc.commons.util.TimeConstants;

/**
 * OutdatedVersion
 * Jan/02/2017 (10:23 PM)
 */

public enum TimeLength
{

    HOUR(TimeConstants.HOUR),
    SECOND(TimeConstants.SECOND),
    HALF_SECOND(TimeConstants.SECOND / 2),
    QUARTER_SECOND(TimeConstants.SECOND / 4),
    TICK(TimeConstants.TICK);

    /** milliseconds to reach this representative unit */
    private final long ms;

    /** the last time this event was hit */
    private long lastCycle;

    /**
     * @param msToHit if we started at {@code 0} what would
     *                we need to get to (in elapsed milliseconds)
     *                to reach this value
     */
    TimeLength(long msToHit)
    {
        this.ms = msToHit;
    }

    public boolean allowTick()
    {
        return true;
    }

}

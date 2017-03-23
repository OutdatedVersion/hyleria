package com.hyleria.common.time;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/15/2017 (12:01 AM)
 */
public enum Time
{

    SECONDS(1),
    MINUTES(60),
    HOURS(3600);

    /** the amount of seconds in this value */
    private final int seconds;

    Time(int seconds)
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

    public static void main(String[] args)
    {
        System.out.println("One Second:");
        System.out.println(Time.SECONDS.toTicks());
        System.out.println();

        System.out.println("One Minute:");
        System.out.println(Time.MINUTES.toTicks());
        System.out.println();

        System.out.println("One Hour:");
        System.out.println(Time.HOURS.toTicks());
        System.out.println();
    }

}

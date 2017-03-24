package com.hyleria.common.redis;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/24/2017 (3:31 PM)
 */
public class RedisChannels
{

    // sharing isn't caring
    private RedisChannels() { }

    /**
     * Where communication that isn't related to any
     * particular topic goes.
     */
    public static final String DEFAULT = channel("gen");

    /**
     * Where primarily backend related chatter goes.
     * For example, the state of a
     */
    public static final String NETWORK = channel("backend");

    /**
     * Returns a channel with the provided
     * name. Of course, our prefix is included.
     *
     * @param val the name
     * @return formatted channel name
     */
    private static String channel(String val)
    {
        return "hyleria-" + val;
    }

}

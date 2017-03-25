package com.hyleria.common.redis;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/24/2017 (3:31 PM)
 */
public enum RedisChannel
{

    /**
     * Where communication that isn't related to any
     * particular topic goes.
     */
    DEFAULT("gen"),

    /**
     * Where primarily backend related chatter goes.
     * For example, the state of a
     */
    NETWORK("backend");

    /** the raw channel */
    public final String channel;

    /**
     * @param val the ending part
     */
    RedisChannel(String val)
    {
        this.channel = channel(val);
    }

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

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
     * @param val the ending part; so we may format
     *            it to how it should be
     */
    RedisChannel(String val)
    {
        this.channel = "hyleria-" + val;
    }

}

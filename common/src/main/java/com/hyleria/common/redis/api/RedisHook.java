package com.hyleria.common.redis.api;

import com.hyleria.common.collection.MapWrapper;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/24/2017 (2:35 PM)
 */
public interface RedisHook
{

    /**
     * Handle a chunk of data from
     * the Redis instance bound to
     * the particular server this
     * was invoked on. Allows for
     * semi-decent real time behavior
     * and quick data caching.
     *
     * @param payload the data
     */
    void handleFromRedis(MapWrapper payload);

}

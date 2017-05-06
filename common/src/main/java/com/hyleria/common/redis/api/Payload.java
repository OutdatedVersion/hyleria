package com.hyleria.common.redis.api;

import com.hyleria.common.redis.RedisChannel;
import com.hyleria.common.redis.RedisHandler;
import org.json.simple.JSONObject;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/24/2017 (3:42 PM)
 */
public interface Payload
{

    /**
     * Turns this payload into a
     * JSON object that we may then
     * send over the provided channel
     *
     * @return the payload as JSON
     */
    JSONObject asJSON();

    /**
     * @return the Redis channel this payload
     *         is to be sent over
     *
     * @see RedisChannel
     */
    RedisChannel channel();

    /**
     * @param focus the {@code focus} of this payload
     * @return the object as a {@link String}
     */
    default String asString(String focus)
    {
        final JSONObject _json = new JSONObject();

        _json.put("focus", focus);

        final JSONObject _payload = this.asJSON();

        if (_payload != null)
            _json.put("payload", _payload);

        return _json.toJSONString();
    }

    /**
     * @param redis the redis instance we're using
     * @return the redis instance that we just
     *         published this payload via
     */
    default RedisHandler publish(RedisHandler redis)
    {
        redis.publish(this.channel().channel, this);
        return redis;
    }

}

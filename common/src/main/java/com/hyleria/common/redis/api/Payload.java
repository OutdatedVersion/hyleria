package com.hyleria.common.redis.api;

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
     * @return the object as a {@link String}
     */
    default String asString()
    {
        return asJSON().toJSONString();
    }

}

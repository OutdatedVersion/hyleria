package com.hyleria.common.backend.payload;

import com.hyleria.common.redis.RedisChannel;
import com.hyleria.common.redis.api.Focus;
import com.hyleria.common.redis.api.Payload;
import org.json.simple.JSONObject;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/29/2017 (5:13 PM)
 */
@Focus ( "gen-self-command-refresh" )
public class RequestSelfCommandRefreshPayload implements Payload
{

    @Override
    public JSONObject asJSON()
    {
        return null;
    }

    @Override
    public RedisChannel channel()
    {
        return RedisChannel.DEFAULT;
    }

}

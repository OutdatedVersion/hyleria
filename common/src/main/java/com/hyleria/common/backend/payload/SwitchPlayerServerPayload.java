package com.hyleria.common.backend.payload;

import com.hyleria.common.json.JSONBuilder;
import com.hyleria.common.redis.RedisChannels;
import com.hyleria.common.redis.api.Focus;
import com.hyleria.common.redis.api.Payload;
import org.json.simple.JSONObject;

import java.util.UUID;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (2:53 PM)
 */
@Focus ( "network-server-switch" )
public class SwitchPlayerServerPayload implements Payload
{

    /** ID */
    public final UUID uuid;

    /** varied ID */
    public final String name;

    /** the server we'd like the player to go to */
    public final String requestedServer;

    /**
     * @param uuid the player's UUID as a string
     * @param name the player's username
     * @param server the server to switch to
     */
    public SwitchPlayerServerPayload(String uuid, String name, String server)
    {
        this(UUID.fromString(uuid), name, server);
    }

    /**
     * @param uuid the player's UUID
     * @param name the player's username
     * @param server the server to switch to
     */
    public SwitchPlayerServerPayload(UUID uuid, String name, String server)
    {
        this.uuid = uuid;
        this.name = name;
        this.requestedServer = server;
    }

    @Override
    public JSONObject asJSON()
    {
        return JSONBuilder.builder()
                        .add("uuid", uuid == null ? null : uuid.toString())
                        .add("name", name)
                        .add("server", requestedServer)
                        .asJSON();
    }

    @Override
    public String channel()
    {
        return RedisChannels.NETWORK;
    }

}

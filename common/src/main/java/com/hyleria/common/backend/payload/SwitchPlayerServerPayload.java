package com.hyleria.common.backend.payload;

import com.hyleria.common.json.JSONBuilder;
import com.hyleria.common.redis.RedisChannel;
import com.hyleria.common.redis.api.Focus;
import com.hyleria.common.redis.api.Payload;
import org.json.simple.JSONObject;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkState;

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
     * Construct a payload who's purpose
     * is to change a proxied player's
     * server from one to another. It'd be
     * preferred for you to use a UUID, but if
     * you can't (why?) then a username MUST
     * be provided.
     *
     * @param uuid the player's UUID
     * @param name the player's username
     * @param server the server to switch to
     */
    public SwitchPlayerServerPayload(UUID uuid, String name, String server)
    {
        this.uuid = uuid;
        this.name = name;
        this.requestedServer = server;

        if (uuid == null)
            checkState(name != null, "Neither a UUID or name was provided");
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
    public RedisChannel channel()
    {
        return RedisChannel.NETWORK;
    }

}

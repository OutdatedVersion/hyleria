package com.hyleria.bungee.network;

import com.google.inject.Inject;
import com.hyleria.common.backend.payload.SwitchPlayerServerPayload;
import com.hyleria.common.redis.RedisChannel;
import com.hyleria.common.redis.RedisHandler;
import com.hyleria.common.redis.api.FromChannel;
import com.hyleria.common.redis.api.HandlesType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (3:55 PM)
 */
public class RequestProcessor
{

    /** BungeeCord proxy */
    @Inject private ProxyServer proxy;

    /**
     * @param redis our redis instance
     */
    @Inject
    public RequestProcessor(RedisHandler redis)
    {
        redis.registerHook(this);
    }

    /**
     * In charge of switching players from one
     * server to another via Redis messages
     *
     * @param payload the switch request payload
     */
    @FromChannel ( RedisChannel.NETWORK )
    @HandlesType ( SwitchPlayerServerPayload.class )
    public void switchServers(SwitchPlayerServerPayload payload)
    {
        ProxiedPlayer _player;

        if (payload.uuid == null)
            _player = proxy.getPlayer(payload.name);
        else
            _player = proxy.getPlayer(payload.uuid);


        if (_player != null)
        {
            final ServerInfo _info = proxy.getServerInfo(payload.requestedServer);

            if (_info != null)
                _player.connect(_info);
        }
    }

}

package com.hyleria.bungee.handle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.bungee.Hyleria;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/25/2017 (12:16 PM)
 */
@Singleton
public class Ping implements Listener
{

    /** local plugin instance */
    @Inject
    private Hyleria hyleria;

    /** the text of our ping response */
    private TextComponent pingResponse = new TextComponent("");

    @EventHandler
    public void handlePing(final ProxyPingEvent event)
    {
        // TODO(Ben): take redis input & allow all of this to be changed via a command

        final ServerPing _response = event.getResponse();

        _response.setDescriptionComponent(pingResponse);
        _response.getPlayers().setMax(500);

        event.setResponse(_response);
    }

}

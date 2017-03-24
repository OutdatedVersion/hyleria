package com.hyleria.bungee.handle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.bungee.Hyleria;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static com.hyleria.bungee.util.Colors.*;
import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.RED;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/25/2017 (12:16 PM)
 */
@Singleton
public class Ping implements Listener
{

    /** local plugin instance */
    @Inject private Hyleria hyleria;

    /** the text of our ping response */
    private TextComponent pingResponse = new TextComponent(GOLD_BOLD + "Hyleria" + GRAY_BOLD + " » " +
            RED + "We're releasing " + DARK_AQUA_BOLD + "Saturday, April 1st" + RED + " at " + AQUA_BOLD + "5PM UTC\n" +
            GRAY + "Visit us at hyleria.com");

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

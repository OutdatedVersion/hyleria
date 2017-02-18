package com.hyleria.module;

import com.google.inject.Inject;
import com.hyleria.commons.inject.Requires;
import com.hyleria.commons.inject.StartParallel;
import com.hyleria.network.AccountManager;
import com.hyleria.util.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;


/**
 * @author Ben (OutdatedVersion)
 * @since Dec/11/2016 (6:53 PM)
 */
@StartParallel
@Requires ( AccountManager.class )
public class Chat extends Module
{

    /** let's us interact with player accounts */
    @Inject
    private AccountManager accountManager;

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event)
    {
        final Player _player = event.getPlayer();

        System.out.println("[Chat] " + _player.getName() + " > " + event.getMessage());
    }

}

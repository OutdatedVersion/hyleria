package net.revellionmc.module;

import com.google.inject.Inject;
import net.revellionmc.network.AccountManager;
import net.revellionmc.util.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * OutdatedVersion
 * Dec/11/2016 (6:53 PM)
 */

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

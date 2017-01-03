package net.revellionmc.module;

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

    public Chat()
    {

    }

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event)
    {
        final Player _player = event.getPlayer();

        System.out.println("[Chat] " + _player.getName() + " > " + event.getMessage());
    }

}

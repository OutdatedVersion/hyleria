package net.hyleriamc.module;

import com.google.inject.Inject;
import net.hyleriamc.commons.util.StartParallelToServer;
import net.hyleriamc.network.AccountManager;
import net.hyleriamc.util.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;


 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/11/2016 (6:53 PM)
  */
 @StartParallelToServer
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

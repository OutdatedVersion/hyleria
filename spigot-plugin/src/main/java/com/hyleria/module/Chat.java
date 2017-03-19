package com.hyleria.module;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.common.account.Account;
import com.hyleria.common.inject.Requires;
import com.hyleria.common.inject.StartParallel;
import com.hyleria.common.reference.Role;
import com.hyleria.network.AccountManager;
import com.hyleria.util.Module;
import com.hyleria.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.BiFunction;

import static com.hyleria.util.RoleFormat.chatFormat;


/**
 * @author Ben (OutdatedVersion)
 * @since Dec/11/2016 (6:53 PM)
 */
@Singleton
@StartParallel
@Requires ( AccountManager.class )
public class Chat extends Module
{

    /** the proper "[Role] Name message" format for messages */
    public static final BiFunction<Player, Account, String> CHAT_PREFIX = (player, account) -> (account.role() != Role.PLAYER ? (chatFormat(account.role()) + " ") : ChatColor.GRAY) + player.getName() + " " + ChatColor.WHITE;

    /** let's us interact with player accounts */
    @Inject private AccountManager accountManager;

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event)
    {
        final Player _player = event.getPlayer();
        final String _message = event.getMessage();
        final Account _account = accountManager.grab(_player);

        String _sentOutMessage = CHAT_PREFIX.apply(_player, _account) + _message;

        PlayerUtil.everyone().forEach(online -> online.sendMessage(_sentOutMessage));

        System.out.println("[Chat] " + _player.getName() + " > " + _message);
        event.setCancelled(true);
    }

}

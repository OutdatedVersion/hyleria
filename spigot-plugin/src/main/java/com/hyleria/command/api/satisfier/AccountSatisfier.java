package com.hyleria.command.api.satisfier;

import com.google.inject.Inject;
import com.hyleria.command.api.ArgumentSatisfier;
import com.hyleria.command.api.Arguments;
import com.hyleria.command.api.data.OnlineOfflinePlayer;
import com.hyleria.common.account.Account;
import com.hyleria.network.AccountManager;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.Scheduler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/31/2017 (5:09 PM)
 */
public class AccountSatisfier implements ArgumentSatisfier<OnlineOfflinePlayer>
{

    /** need to access this */
    @Inject private AccountManager accountManager;

    @Override
    public OnlineOfflinePlayer get(Player player, Arguments args)
    {
        final String _name = args.next();
        Player _onlineTry = PlayerUtil.search(player, _name, true);
        AtomicReference<Account> account = new AtomicReference<>();

        if (_onlineTry == null)
        {
            Scheduler.async(() -> account.lazySet(accountManager.grab(player.getUniqueId())));
        }

        return new OnlineOfflinePlayer(_onlineTry, account.get());
    }

    @Override
    public String fail(String provided)
    {
        return "Couldn't find any online/offline player matching: " + ChatColor.YELLOW + provided;
    }

    @Override
    public Class<OnlineOfflinePlayer> satisfies()
    {
        return OnlineOfflinePlayer.class;
    }

}

package com.hyleria.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.common.account.Account;
import com.hyleria.common.inject.StartParallel;
import com.hyleria.common.mongo.Database;
import com.hyleria.util.Issues;
import com.hyleria.util.LogUtil;
import com.hyleria.util.Module;
import com.hyleria.util.ShutdownHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.UUID;


/**
 * In charge of loading & removing
 * accounts.
 *
 * @author Ben (OutdatedVersion)
 * @since Dec/11/2016 (6:54 PM)
 */
@Singleton
@StartParallel
public class AccountManager extends Module
{

    /** allows us to grab info from our mongo instance */
    @Inject private Database database;

    /**
     * Attempts to grab an account by
     * a Bukkit {@link Player}.
     *
     * @param player the player
     * @return an account for that player
     */
    public Account grab(Player player)
    {
        return grab(player.getUniqueId());
    }

    /**
     * Attempts to find the account of
     * a player via a username
     *
     * @param name the name of the player
     * @return the account bound to that name
     */
    public Account grab(String name)
    {
        return database.cacheFetch(name).orElseThrow(() -> new RuntimeException("Missing account for [" + name + "]"));
    }

    /**
     * Attempts to grab the account
     * for the provided player. When
     * we fail to do so an exception
     * will be thrown.
     *
     * @param uuid UUID of the player's account
     *             that we're looking for
     * @return the account
     */
    public Account grab(UUID uuid)
    {
        return database.cacheFetch(uuid).orElseThrow(() -> new RuntimeException("Missing account for [" + uuid.toString() + "]"));
    }

    @ShutdownHook
    public void closeDatabase()
    {
        database.releaseResources();
    }

    @EventHandler
    public void handleLogin(AsyncPlayerPreLoginEvent event)
    {
        try
        {
            final long _startedAt = System.currentTimeMillis();
            final Optional<Account> _transaction = database.fetchAccountSync(event.getUniqueId());

            System.out.println(database.cache().toString());

            if (_transaction.isPresent())
            {
                System.out.println(_transaction.get().toString());
                database.cacheCommit(_transaction.get());
                // TODO(Ben): update account | name, IP
            }
            else
            {
                System.out.println("NEW ACCOUNT");

                final Account _account = Account.fromLoginData(event.getUniqueId(), event.getName(), event.getAddress().getHostAddress());

                database.cacheCommit(_account);
                database.accounts.insertOne(_account.asDocument());
            }

            LogUtil.system("Login", "Elapsed for " + event.getName() + ": " + (System.currentTimeMillis() - _startedAt) + "ms");
        }
        catch (Exception ex)
        {
            Issues.handle("Player Login", ex);
        }
    }

    @EventHandler
    public void cleanupCache(PlayerQuitEvent event)
    {
        database.cacheInvalidate(event.getPlayer().getUniqueId());
    }

}

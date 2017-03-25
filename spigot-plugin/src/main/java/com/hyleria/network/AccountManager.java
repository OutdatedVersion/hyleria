package com.hyleria.network;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.common.account.Account;
import com.hyleria.common.inject.StartParallel;
import com.hyleria.common.mongo.Database;
import com.hyleria.common.reference.Role;
import com.hyleria.network.login.LoginHook;
import com.hyleria.util.Issues;
import com.hyleria.util.LogUtil;
import com.hyleria.util.Module;
import com.hyleria.util.ShutdownHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
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

    /**  */
    private List<LoginHook> loginHooks = Lists.newArrayList();

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

    @EventHandler ( priority = EventPriority.LOW )
    public void handleLogin(AsyncPlayerPreLoginEvent event)
    {
        try
        {
            if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
                return;


            final long _startedAt = System.currentTimeMillis();
            final Optional<Account> _transaction = database.fetchAccountSync(event.getUniqueId());

            if (_transaction.isPresent())
            {
                database.cacheCommit(_transaction.get());
                // TODO(Ben): update account | name, IP
            }
            else
            {
                final Account _account = Account.fromLoginData(event.getUniqueId(), event.getName(), event.getAddress().getHostAddress());

                database.cacheCommit(_account);
                database.accounts.insertOne(_account.asDocument());
            }

            LogUtil.system("Login", "Elapsed time for " + event.getName() + ": " + (System.currentTimeMillis() - _startedAt) + "ms");
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

    @EventHandler
    public void grantOperator(PlayerLoginEvent event)
    {
        // Admin+ is granted OP

        if (grab(event.getPlayer()).role().ordinal() <= Role.ADMIN.ordinal())
            event.getPlayer().setOp(true);
    }

}

package net.revellionmc.network;

import com.google.inject.Inject;
import net.revellionmc.network.database.Account;
import net.revellionmc.network.database.Database;
import net.revellionmc.util.Issues;
import net.revellionmc.util.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * OutdatedVersion
 * Dec/11/2016 (6:54 PM)
 */

public class AccountManager extends Module
{

    /** allows us to grab info from our mongo instance */
    private final Database database;

    /**
     * Handles anything directly related to accounts.
     *
     * Includes:
     *  - fetching data
     *  - updating data
     *
     * @param database our database instance
     */
    @Inject
    public AccountManager(Database database)
    {
        this.database = database;
    }

    @EventHandler
    public void handleLogin(AsyncPlayerPreLoginEvent event)
    {
        try
        {
            database.fetchAccountSync(event.getUniqueId(), callback ->
            {
                // they've been on before..
                if (callback.isPresent())
                {
                    final Account _account = callback.get();

                    database.cacheCommit(_account);
                    // TODO(Ben): update account
                }
                // insert into database
                else
                {
//                    Account.create(event.getUniqueId(), event.getName(), event.getAddress().getHostName());
                }
            });
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

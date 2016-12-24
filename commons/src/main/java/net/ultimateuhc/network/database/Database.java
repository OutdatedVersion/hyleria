package net.ultimateuhc.network.database;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.ultimateuhc.util.MongoUtil;
import net.ultimateuhc.util.config.Configurations;
import org.bson.Document;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

/**
 * OutdatedVersion
 * Dec/08/2016 (8:07 PM)
 */

public class Database
{

    /** our one and only Mongo client */
    private final MongoClient client;

    /** the only Mongo database we're using */
    public final MongoDatabase database;

    /** the collection accounts are stored in (used for most things) */
    public final MongoCollection<Document> accounts;

    /** local cache for accounts | it is crucial that you properly handle the invalidation of items here. */
    private Cache<UUID, Account> accountCache;

    /** run all database requests async */
    private ExecutorService executor;

    /**
     * @param config the minimal name of our config file
     *               for this database instance.
     */
    public Database(String config)
    {
        final DatabaseConfig _config = Configurations.read("database/" + config, DatabaseConfig.class);

        client = new MongoClient(new ServerAddress(_config.connection.host, _config.connection.port),
                                 Collections.singletonList(MongoCredential.createCredential(_config.auth.username, _config.database, _config.auth.password.toCharArray())));


        database = client.getDatabase(_config.database);
        accounts = database.getCollection(_config.collection);

        executor = Executors.newCachedThreadPool();

        if (_config.cacheSpecification != null)
            accountCache = Caffeine.from(_config.cacheSpecification).build();
        else
            accountCache = Caffeine.newBuilder().build();
    }

    /**
     * Unbind the allocated resources for
     * this database instance.
     *
     * Our {@link #executor} will finish executing
     * the set of tasks it currently has queued.
     */
    public void releaseResources()
    {
        client.close();
        executor.shutdown();
    }

    /**
     * @return our local cache containing loaded accounts
     */
    public Cache<UUID, Account> cache()
    {
        return accountCache;
    }

    /**
     * Checks is a player is in our cache
     *
     * @param username the username bound to the account
     *                 that we're looking for
     *
     * @return yes ({@code true} or no ({@code false})
     */
    public boolean cacheContains(String username)
    {
        return accountCache.asMap().entrySet().stream().anyMatch(entry -> entry.getValue().username().equals(username));
    }

    /**
     * Check if a player (by UUID)
     * is in our cache
     *
     * @param uuid the UUID bound to the account
     *             that we're looking for
     *
     * @return yes or no
     */
    public boolean cacheContains(UUID uuid)
    {
        return accountCache.asMap().containsKey(uuid);
    }

    /**
     * Removes the specifies account (found by UUID)
     * from our cache. Assumes they're in it.
     *
     * @param uuid the player's UUID
     * @return the
     */
    public Database cacheInvalidate(UUID uuid)
    {
        accountCache.invalidate(uuid);
        return this;
    }

    /**
     * Inserts an account into our cache
     *
     * @param account the account
     * @return the account that was just inserted
     */
    public Account cacheCommit(Account account)
    {
        accountCache.put(account.uuid(), account);
        return account;
    }

    /**
     * Grabs an account from our cache wrapped
     * in an {@link Optional}
     *
     * @param username the username
     * @return the account or an empty Optional
     */
    public Optional<Account> cacheFetch(String username)
    {
        return accountCache.asMap().entrySet()
                                   .stream()
                                   .filter(entry -> entry.getValue().username().equalsIgnoreCase(username))
                                   .findFirst()
                                   .flatMap(entry -> Optional.ofNullable(entry.getValue()));
    }

    /**
     * Grabs an account from out cache based
     * on a player's UUID.
     *
     * @param uuid the UUID
     * @return an account wrapped in an {@linkplain Optional}
     */
    public Optional<Account> cacheFetch(UUID uuid)
    {
        return Optional.ofNullable(accountCache.asMap().get(uuid));
    }

    /**
     * Grab an account from our database via
     * a username.
     *
     * @param username the username
     * @param callback our account
     */
    public void fetchAccount(String username, Consumer<Optional<Account>> callback)
    {
        fetchAccount(null, username, true, true, callback);
    }

    /**
     * Grab an account from our database via
     * the UUID provided.
     *
     * @param uuid the UUID
     * @param callback our account
     */
    public void fetchAccount(UUID uuid, Consumer<Optional<Account>> callback)
    {
        fetchAccount(uuid, null, true, true, callback);
    }

    /**
     * Grab an account from our database
     * synchronously. Probably only ever
     * going to be used when someone is
     * logging into a server.
     *
     * @param uuid the UUID of the player
     * @param callback the player's account
     */
    public void fetchAccountSync(UUID uuid, Consumer<Optional<Account>> callback)
    {
        fetchAccount(uuid, null, true, false, callback);
    }

    /**
     * Internal Method
     *
     * Loads an account from our main Mongo
     * database & wraps it in an {@linkplain Optional}
     * or returns an empty one if the player
     * doesn't exist within the account collection.
     *
     * This method takes both a username & UUID.
     * Saves on some duplicated code.
     * Use the overloaded methods instead.
     *
     * @param uuid if we're looking someone up by UUID..
     * @param username if we're looking someone up by username..
     * @param useCache do we want to check our cache?
     * @param callback our requested data
     */
    private void fetchAccount(UUID uuid, String username, boolean useCache, boolean async, Consumer<Optional<Account>> callback)
    {
        boolean _useUsername = uuid == null && username != null;

        // try locally first if we want to
        if (useCache)
        {
            Optional<Account> _cacheHit;

            if (_useUsername)
                _cacheHit = cacheFetch(username);
            else
                _cacheHit = cacheFetch(uuid);

            if (_cacheHit.isPresent())
            {
                callback.accept(_cacheHit);
                return;
            }
        }

        // code to make the database transaction
        final Runnable _fetchTask = () ->
        {
            Document _document = accounts.find(!_useUsername
                                               ? eq("uuid", uuid.toString())
                                               : eq("username_lower", username.toLowerCase())).limit(1).first();

            callback.accept(_document == null ? Optional.empty()
                                              : Optional.ofNullable(MongoUtil.translate(Account.DEFAULTS, _document)));
        };

        // in case we need to call this when
        // logging in or a similar situation
        if (async)
            executor.submit(_fetchTask);
        else
            _fetchTask.run();
    }

}

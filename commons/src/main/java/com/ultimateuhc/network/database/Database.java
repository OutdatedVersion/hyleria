package com.ultimateuhc.network.database;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ultimateuhc.util.config.Configurations;
import org.bson.Document;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

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

    /**
     * @param config the minimal name of our config file
     *               for this database instance.
     */
    public Database(String config)
    {
        final DatabaseConfig _config = Configurations.read(config, DatabaseConfig.class);

        client = new MongoClient(new ServerAddress(_config.connection.host, _config.connection.port),
                                 Collections.singletonList(MongoCredential.createCredential(_config.auth.username, _config.database, _config.auth.password.toCharArray())));


        database = client.getDatabase(_config.database);
        accounts = database.getCollection(_config.collection);


        if (_config.cacheSpecification != null)
            accountCache = Caffeine.from(_config.cacheSpecification).build();
        else
            accountCache = Caffeine.newBuilder().build();
    }

    /**
     * Unbind the allocated resources for
     * this database instance.
     */
    public void releaseResources()
    {
        client.close();
    }

    private void fetchAccount(UUID uuid, String username, Consumer<Optional<Account>> callback)
    {
        boolean _useUsername = uuid == null && username != null;

    }

}

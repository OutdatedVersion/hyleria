package com.ultimateuhc.network.database;

import com.google.gson.annotations.SerializedName;

/**
 * OutdatedVersion
 * Dec/10/2016 (11:57 PM)
 */

public class DatabaseConfig
{

    /** the details we use to open a connection */
    @SerializedName ( "connection_details" )
    public ConnectionDetails connection;

    /** our connection details */
    public AuthDetails auth;

    /** the database we're using */
    public String database;

    /** the main mongo collection we're using */
    public String collection;

    /** details for our Guava cache | {@link null} if you wouldn't like to provide these details */
    @SerializedName( "cache_spec" )
    public String cacheSpecification;

    /**
     * Represents a set of info holding
     * the information for a basic
     * MongoDB connection.
     */
    public class ConnectionDetails
    {
        public String host;
        public int port;
    }

    /**
     * Represents a set of credentials
     * used to connect to our {@link #database}.
     */
    public class AuthDetails
    {
        public String username;
        public String password;
    }

}

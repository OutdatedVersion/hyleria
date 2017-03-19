package com.hyleria.common.account;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import com.hyleria.common.mongo.document.DocumentBuilder;
import com.hyleria.common.mongo.document.DocumentCompatible;
import com.hyleria.common.reference.Role;
import org.bson.Document;

import java.util.List;
import java.util.UUID;


/**
  * @author Ben (OutdatedVersion)
  * @since Dec/08/2016 (8:15 PM)
  */
@SuppressWarnings ( "unchecked" )
public class Account implements DocumentCompatible
{

    private UUID uuid;

    private String name;

    @SerializedName ( "previous_names" )
    private List<String> previousUsernames = Lists.newArrayList();

    private Role role = Role.PLAYER;

    // TODO(Ben): currency & XP?

    @SerializedName ( "current_address" )
    private String currentIP;

    @SerializedName ( "previous_addresses" )
    private List<PreviousAddress> previousAddresses = Lists.newArrayList();

    /** get -> {@link #uuid} */
    public UUID uuid()
    {
        return uuid;
    }

    /** get -> {@link #name} */
    public String username()
    {
        return name;
    }

    /** get -> {@link #role} */
    public Role role()
    {
        return role;
    }


    /**
     * Represents some other IP that
     * someone logged in from.
     */
    public static class PreviousAddress
    {
        /** the IP */
        public String value;

        /** the UNIX epoch timestamp we last saw this on */
        public long lastUsedOn;

        public PreviousAddress(String value, long lastUsedOn)
        {
            this.value = value;
            this.lastUsedOn = lastUsedOn;
        }
    }

    @Override
    public Document asDocument()
    {
        return DocumentBuilder.create()
                .withFreshDoc()
                .skipOver("uuid")  // we want to insert this on our own
                .appendAllFields(this)
                .append("uuid", this.uuid.toString())
                .append("name_lower", this.name.toLowerCase())
                .finished();
    }

    @Override
    public Account populateFromDocument(Document document)
    {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.name = document.getString("name");
        this.role = Role.valueOf(document.getString("role"));
        this.previousUsernames = document.get("previous_names", List.class);
        this.currentIP = document.getString("current_address");
        this.previousAddresses = document.get("previous_addresses", List.class);

        return this;
    }

    /**
     * Turns the provided data, retrieved
     * from a client when they login, into
     * a basic player {@link Account}.
     *
     * @param uuid the player's UUID
     * @param name the player's username
     * @param ip the player's IP address
     * @return the new account
     */
    public static Account fromLoginData(UUID uuid, String name, String ip)
    {
        final Account _fresh = new Account();

        _fresh.uuid = uuid;
        _fresh.name = name;
        _fresh.currentIP = ip;

        return _fresh;
    }

}

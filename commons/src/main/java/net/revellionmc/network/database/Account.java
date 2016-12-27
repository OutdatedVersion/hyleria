package net.revellionmc.network.database;

import com.google.gson.annotations.SerializedName;
import net.revellionmc.util.Role;

import java.util.List;
import java.util.UUID;

/**
 * OutdatedVersion
 * Dec/08/2016 (8:15 PM)
 */

public class Account
{

    /** an account with all of the values of a fresh account */
    public static final Account DEFAULTS = new Account();

    // populate the default account w/ the proper values
    static
    {
        DEFAULTS.role = Role.PLAYER;
    }


    private UUID uuid;

    @SerializedName ( "name" )
    private String username;

    @SerializedName ( "previous_names" )
    private List<String> previousUsernames;

    private Role role;
    private List<String> privileges;
    private List<Package> packages;

    // TODO(Ben): currency & XP?

    @SerializedName ( "current_address" )
    private String currentIP;

    @SerializedName ( "previous_addresses" )
    private List<PreviousAddress> previousAddresses;

    /** get -> {@link #uuid} */
    public UUID uuid()
    {
        return uuid;
    }

    /** get -> {@link #username} */
    public String username()
    {
        return username;
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
    }

}

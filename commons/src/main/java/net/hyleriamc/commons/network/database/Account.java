package net.hyleriamc.commons.network.database;

import com.google.gson.annotations.SerializedName;
import net.hyleriamc.commons.util.Role;

import java.lang.*;
import java.util.List;
import java.util.UUID;


 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/08/2016 (8:15 PM)
  */
public class Account
{

    private UUID uuid;

    private String name;

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

    /** get -> {@link #name} */
    public String username()
    {
        return name;
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

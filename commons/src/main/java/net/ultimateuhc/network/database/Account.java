package net.ultimateuhc.network.database;

import net.ultimateuhc.util.Role;

import java.util.List;
import java.util.UUID;

/**
 * OutdatedVersion
 * Dec/08/2016 (8:15 PM)
 */

public class Account
{

    private UUID uuid;

    private String username;
    private List<String> previousUsernames;

    private Role role;
    private List<String> privileges;
    private List<Package> packages;

    // TODO(Ben): currency & XP?

    private String currentIP;
    private List<PreviousAddress> previousAddresses;

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

}

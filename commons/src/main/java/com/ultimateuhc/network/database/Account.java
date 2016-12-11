package com.ultimateuhc.network.database;

import com.ultimateuhc.util.Rank;

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

    private Rank permissionRank;
    private List<Rank> packageRanks;

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

}

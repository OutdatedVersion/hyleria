package com.hyleria.commons.network.database;

import com.google.gson.annotations.SerializedName;
import com.simplexitymc.util.json.Exclude;

 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/13/2016 (4:46 PM)
  */
public class Package
{

    /** the name that we use internally for this package */
    @SerializedName ( "key" )
    public final String privateName;

    /** the user facing name for this package */
    @Exclude
    public final String publicName;

    /**
     * Represents some "package".
     *
     * It may be a donator rank, perk,
     * in-game perk. etc
     *
     * @param privateName internal name
     * @param publicName "friendly" name
     */
    public Package(String privateName, String publicName)
    {
        this.privateName = privateName;
        this.publicName = publicName;
    }

}

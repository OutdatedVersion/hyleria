package com.hyleria.common.reference;

import org.apache.commons.lang3.text.WordUtils;

/**
  * @author Ben (OutdatedVersion)
  * @since Dec/10/2016 (12:19 PM)
  */
public enum Role
{

    DEV('c'),
    ADMIN('c'),
    SENIOR('d'),    // full staff everywhere
    FULL('5'),      // takes in "staff_focus"
    TRIAL('6'),     // ^
    MEDIA('9'),     // takes in "media_type"
    PLAYER('7');

    /** the color of the tag */
    public char colorCode;

    /** the public-facing name for this role */
    public String name;

    Role(char colorCode)
    {
        this(colorCode, null);
    }

    Role(char colorCode, String displayName)
    {
        this.colorCode = colorCode;
        this.name = displayName == null ? WordUtils.capitalizeFully(this.name().replaceAll("_", " "))
                                        : displayName;
    }

    /**
     * @return this name's public appearance (w/o a color)
     */
    public String toNameColorless()
    {
        return String.format("[%s]", this.name());
    }

    /**
     * @return where we store the data the "special" roles need
     */
    public String additionalData()
    {
       switch (this)
       {
           case FULL:
           case TRIAL:
               return "staff_game_focus";

           case MEDIA:
               return "media_type";

           default:
               return null;
       }
    }

}

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
    SENIOR('b'),
    MOD('9'),
    TRIAL('6'),     // takes in "staff_focus"
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

}

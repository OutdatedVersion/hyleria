package com.hyleria.commons.reference;


 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/10/2016 (12:19 PM)
  */
public enum Role
{

    ADMIN('c'),
    SENIOR('b'),
    MOD('9'),
    TRIAL('6'),
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
        this.name = displayName == null ? this.name() : displayName;
    }

    /**
     * @return this name's public appearance (w/o a color)
     */
    public String toNameColorless()
    {
        return String.format("[%s]", this.name());
    }

}

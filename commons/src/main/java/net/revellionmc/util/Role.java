package net.revellionmc.util;

/**
 * OutdatedVersion
 * Dec/10/2016 (12:19 PM)
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

    Role(char colorCode)
    {
        this.colorCode = colorCode;
    }

    /**
     * @return the result of {@link #toNameColorless()} with
     *         the fancy color we use
     */
    public String toName()
    {
        return '§' + this.colorCode + toNameColorless();
    }

    /**
     * @return this name's public appearance (w/o a color)
     */
    public String toNameColorless()
    {
        return String.format("[%s]", this.name());
    }

}
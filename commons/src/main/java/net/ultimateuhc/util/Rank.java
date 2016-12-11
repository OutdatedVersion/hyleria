package net.ultimateuhc.util;

/**
 * OutdatedVersion
 * Dec/10/2016 (12:19 PM)
 */

public enum Rank
{

    OWNER('c'),
    DEV('c'),
    ADMIN('c'),
    SENIOR('b'),
    MOD('9'),
    TRIAL('6'),
    PLAYER('7');

    /** the color of the rank's tag */
    public char colorCode;

    Rank(char colorCode)
    {
        this.colorCode = colorCode;
    }

    /**
     * @return the result of {@link #toNameColorless()} with
     *         the fancy color this rank uses
     */
    public String toName()
    {
        return '§' + this.colorCode + toNameColorless();
    }

    /**
     * @return the result of {@link #formatName()} for this rank
     */
    public String toNameColorless()
    {
        switch (this)
        {
            case OWNER:
            case DEV:
                return Rank.ADMIN.formatName();

            default:
                return formatName();
        }
    }

    /**
     * @return the rank we're currently referencing as a
     *         friendly name people will recognize
     */
    private String formatName()
    {
        return String.format("[%s]", this.name());
    }

}

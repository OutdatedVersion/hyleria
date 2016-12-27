package net.revellionmc.util;

/**
 * OutdatedVersion
 * Dec/23/2016 (8:07 PM)
 */

public abstract class Translator<C, D>
{

    /** the type of this when read from the source */
    C codeSafe;

    /** the type of this when written to the database */
    D databaseSafe;

    /**
     * In charge of translating the
     * database version of this into
     * the code version
     *
     * @param raw what we're translating
     * @return the code version
     */
    public abstract C read(D raw);

    /**
     * In charge of creating a database
     * safe version of what we're working with
     *
     * @param raw what we're working with
     * @return the database safe version of it
     */
    public abstract D write(C raw);

}

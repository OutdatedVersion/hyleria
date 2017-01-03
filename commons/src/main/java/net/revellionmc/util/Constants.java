package net.revellionmc.util;

import java.util.regex.Pattern;

/**
 * OutdatedVersion
 * Jan/02/2017 (9:57 PM)
 */

public class Constants
{

    /** where the Minecraft related items are stored */
    public static final String BASE_PATH = "/home/mc/";

    /** where we store configuration files */
    public static final String DATA_FOLDER = BASE_PATH + "configuration/";

    /** where the hard Minecraft servers are located */
    public static final String BASE_SERVER_FOLDER = BASE_PATH + "network/prod/";

    /** allows us to check that a {@link String} is in fact an {@link java.util.UUID} */
    public static Pattern UUID_REGEX = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

}

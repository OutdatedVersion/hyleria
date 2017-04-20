package com.hyleria.common.reference;

import com.google.inject.Stage;


/**
 * @author Ben (OutdatedVersion)
 * @since Jan/02/2017 (9:57 PM)
 */
public class Constants
{

    /** the current environment of this place. represented by a {@link Stage} from Guice. */
    public static final Stage ENV = Stage.valueOf(System.getProperty("env", "DEVELOPMENT").toUpperCase());

    /** where the Minecraft related items are stored */
    public static final String BASE_PATH = "/home/mc/";

    /** where we store configuration files */
    public static final String DATA_FOLDER = BASE_PATH + "config/";

    /** where the hard Minecraft servers are located */
    public static final String BASE_SERVER_FOLDER = BASE_PATH + "network/prod/";

}

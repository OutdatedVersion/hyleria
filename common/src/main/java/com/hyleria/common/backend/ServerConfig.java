package com.hyleria.common.backend;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (4:49 PM)
 */
public class ServerConfig
{

    /** the name of the file that this is usually saved as */
    public static final String FILE_NAME = "server_config.json";

    /** the public facing name for this server (also serves as the unique ID) */
    public String name;

    /** the game that will be loaded on this server */
    public String forcedGame;

    /** data relating to the game currently going */
    public GameConfiguration gameConfig;

}

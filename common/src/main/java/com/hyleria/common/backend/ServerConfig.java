package com.hyleria.common.backend;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (4:49 PM)
 */
public class ServerConfig
{

    /** the public facing name for this server (also serves as the unique ID) */
    public String name;

    /** the game that will be loaded on this server */
    public String forcedGame;

    /** data relating to the game currently going */
    public GameConfiguration gameConfig;

}

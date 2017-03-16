package com.hyleria.common.backend;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName ( "forced_game" )
    public String forcedGame;

    /** the type of game config we're using | see {@link GameConfiguration} for details */
    @SerializedName ( "game_config_mode" )
    public ConfigurationMode configMode;

}

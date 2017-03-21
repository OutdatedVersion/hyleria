package com.hyleria.coeus.available.uhcgames;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * @author Jp78
 * @since Sunday Mar/17
 */
public class UHCGamesConfig
{

    /**
     * The <strong>CODE</strong> names of the enabled maps. This must correspond to the maps package in uhcg.world
     */
    @SerializedName ( "enabled_maps" )
    public Set<String> maps;

}

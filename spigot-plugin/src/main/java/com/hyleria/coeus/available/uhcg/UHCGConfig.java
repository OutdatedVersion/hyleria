package com.hyleria.coeus.available.uhcg;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * This file was created by @author thejp for the use of
 * hyleria. Please note, all rights to code are retained by
 * afore mentioned thejp unless otherwise stated.
 * File created: Sunday, March, 2017
 */
public class UHCGConfig
{

    /**
     * The <Strong>CODE</Strong> names of the enabled maps. This must correspond to the maps package in uhcg.world
     */
    @SerializedName("enabled_maps")
    public Set<String> maps;
}

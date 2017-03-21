package com.hyleria.coeus.available.uhcg.world.maps;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Set;

/**
 * This file was created by @author thejp for the use of
 * hyleria. Please note, all rights to code are retained by
 * afore mentioned thejp unless otherwise stated.
 * File created: Sunday, March, 2017
 */
public interface UHCGMap
{

    /**
     * Get the world associated with this map
     *
     * @return The world
     */
    World world();

    /**
     * Get the spawn locations associated with this map
     */
    Set<Location> spawns();

    /**
     * Amount of allowed players in this map. Techincally, this should be how many spawn locations there are but it may not be. In that case, override this.
     * @return The amount of allowed players
     */
    default Integer allowedPlayers()
    {
        return spawns().size();
    }

    /**
     * Get a list of all the <strong>high {@link com.hyleria.coeus.available.uhcg.utils.Tier} chests (High quality)</strong> in the map. Typically these should be spawn chests.
     * @return Said list
     */
    List<Location> highChests();
}

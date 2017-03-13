package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Diamond ore no longer drops diamonds,
 * and players (when killed by another
 * player) drop a diamond.
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (12:06 AM)
 */
public class Diamondless extends UHCScenario
{

    @Override
    public String name()
    {
        return "Diamondless";
    }

    @EventHandler
    public void removeDrops(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.DIAMOND_ORE)
            event.getBlock().getDrops().clear();
    }

    // handle the dropping

}

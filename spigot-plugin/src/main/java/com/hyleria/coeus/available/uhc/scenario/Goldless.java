package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * The ability to mine gold pieces
 * is stripped, and you may only
 * obtain them via killing a player;
 * which now drops one!
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (12:05 AM)
 */
public class Goldless extends UHCScenario
{

    @Override
    public String name()
    {
        return "Goldless";
    }

    @EventHandler
    public void removeDrops(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.GOLD_ORE)
            event.getBlock().getDrops().clear();
    }

    // handle some CombatDeathEvent or etc

}

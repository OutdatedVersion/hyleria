package com.hyleria.coeus.available.uhc.scenario;

import com.hyleria.commons.math.Math;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import static com.hyleria.coeus.available.uhc.scenario.Cutclean.APPLE;
import static com.hyleria.coeus.available.uhc.scenario.Cutclean.FLINT;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/12/2017 (10:38 AM)
 */
public class VanillaPlus extends UHCScenario
{

    @Override
    public String name()
    {
        return "Vanilla+";
    }

    @EventHandler
    public void modifyDrops(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.GRAVEL)
        {
            event.getBlock().getDrops().clear();

            if (Math.chance(20))
                event.getBlock().getDrops().add(FLINT);
        }
    }

    @EventHandler
    public void leaves(LeavesDecayEvent event)
    {
        event.getBlock().getDrops().clear();

        if (Math.chance(1.5))
            event.getBlock().getDrops().add(APPLE);
    }

}

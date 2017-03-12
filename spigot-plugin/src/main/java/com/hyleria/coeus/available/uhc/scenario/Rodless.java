package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

/**
 * Prevent the crafting of fishing rods
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (8:22 PM)
 */
public class Rodless extends UHCScenario
{

    @Override
    public String name()
    {
        return "Rodless";
    }

    @EventHandler
    public void preventCrafting(PrepareItemCraftEvent event)
    {
        if (event.getInventory().getResult().getType() == Material.FISHING_ROD)
            event.getInventory().setResult(null);
    }

}

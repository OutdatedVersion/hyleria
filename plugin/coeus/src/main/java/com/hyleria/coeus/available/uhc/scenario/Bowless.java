package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

/**
 * Bows are not craftable & skeletons
 * are kept from dropping them
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (8:31 PM)
 */
public class Bowless extends UHCScenario
{

    @Override
    public String name()
    {
        return "Bowless";
    }

    @EventHandler
    public void nonDropping(EntityDeathEvent event)
    {
        if (event.getEntityType() == EntityType.SKELETON)
            event.getDrops().removeIf(stack -> stack.getType() == Material.BOW);
    }

    @EventHandler
    public void nonCraftable(PrepareItemCraftEvent event)
    {
        if (event.getInventory().getResult().getType() == Material.BOW)
            event.getInventory().setResult(null);
    }

}

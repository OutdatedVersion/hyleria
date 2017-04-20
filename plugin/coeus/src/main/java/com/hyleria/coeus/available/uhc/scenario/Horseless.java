package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;
import org.spigotmc.event.entity.EntityMountEvent;

/**
 * Disallows the taming & riding of horses
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (8:36 PM)
 */
public class Horseless extends UHCScenario
{

    @Override
    public String name()
    {
        return "Horseless";
    }

    @EventHandler
    public void preventTaming(EntityTameEvent event)
    {
        if (event.getEntityType() == EntityType.HORSE)
            event.setCancelled(true);
    }

    @EventHandler
    public void preventMounting(EntityMountEvent event)
    {
        if (event.getEntityType() == EntityType.HORSE)
            event.setCancelled(true);
    }

}

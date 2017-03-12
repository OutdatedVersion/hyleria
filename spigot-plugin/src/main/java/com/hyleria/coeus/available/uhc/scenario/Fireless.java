package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * You may not take damage from anything
 * flame related.
 *
 *
 * <strong>Including:</strong>
 * <ul>
 *     <ul>Lava</ul>
 *     <ul>Flames (includes flint and steel & damage done per tick by player ignition)</ul>
 *     <ul>Fire Aspect</ul>
 * </ul>
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (8:40 PM)
 */
public class Fireless extends UHCScenario
{

    @Override
    public String name()
    {
        return "Fireless";
    }

    @EventHandler
    public void disableFireDamage(EntityDamageEvent event)
    {
        // is fire aspect combustion handled separately?

        if (event.getEntityType() != EntityType.PLAYER)
            return;

        if (   event.getCause() == EntityDamageEvent.DamageCause.FIRE
            || event.getCause() == EntityDamageEvent.DamageCause.LAVA
            || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
            event.setCancelled(true);
    }

}

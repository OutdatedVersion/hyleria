package com.hyleria.coeus.available.uhc.scenario;

import com.hyleria.coeus.damage.kinds.FireDamageEvent;
import com.hyleria.coeus.damage.kinds.LavaDamageEvent;
import org.bukkit.event.EventHandler;

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
    public void disableFire(FireDamageEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void disableLava(LavaDamageEvent event)
    {
        event.setCancelled(true);
    }

}

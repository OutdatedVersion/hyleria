package com.simplexitymc.kraken.damage;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * OutdatedVersion
 * At: 1:02 PM (May/23/2016)
 * cave-realms
 */

public class DamageDeathEvent extends PlayerEvent
{
    private static final HandlerList handlers = new HandlerList();

    private final DamageLog log;

    public DamageDeathEvent(Player who, DamageLog log)
    {
        super(who);

        this.log = log;
    }

    public DamageLog getLog()
    {
        return log;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}

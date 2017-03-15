package com.simplexitymc.kraken.damage;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * OutdatedVersion
 * At: 11:05 PM (May/07/2016)
 * cave-realms
 */

public class CombatDeathEvent extends PlayerEvent
{
    private static final HandlerList handlers = new HandlerList();

    private final DamageLog log;
    private final Player finalAttacker;

    public CombatDeathEvent(Player who, Player lastDamageDealtBy, DamageLog log)
    {
        super(who);

        this.log = log;
        this.finalAttacker = lastDamageDealtBy;
    }

    public Player getBy()
    {
        return finalAttacker;
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

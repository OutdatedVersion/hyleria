package com.simplexitymc.kraken.damage;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * OutdatedVersion
 * At: 10:10 PM (May/07/2016)
 * cave-realms
 */

public abstract class DamageEvent extends Event implements Cancellable
{

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;

    private final Player victim;
    private final double dealtDamage;
    private final DamageCause damageCause;

    public DamageEvent(Player victim, double dealtDamage, DamageCause damageCause)
    {
        this.victim = victim;
        this.dealtDamage = dealtDamage;
        this.damageCause = damageCause;
    }

    public Player getVictim()
    {
        return victim;
    }

    public double getDamageDealt()
    {
        return dealtDamage;
    }

    public DamageCause getDamageCause()
    {
        return damageCause;
    }

    public abstract BaseComponent[] getInformation();

    @Override
    public void setCancelled(boolean val)
    {
        this.isCancelled = val;
    }

    @Override
    public boolean isCancelled()
    {
        return isCancelled;
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

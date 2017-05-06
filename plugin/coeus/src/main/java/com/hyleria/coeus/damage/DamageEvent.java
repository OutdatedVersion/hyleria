package com.hyleria.coeus.damage;

import com.hyleria.util.HyleriaEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Some sort of damage was inflicted
 * onto a player. Let's handle that.
 *
 * @author Ben (OutdatedVersion)
 * @since 10:10 PM (May/07/2016)
 */
public abstract class DamageEvent extends HyleriaEvent implements Cancellable
{

    /** Bukkit */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /** whether or not this event will be fully propagated */
    private boolean isCancelled;

    /** who this is happening to */
    private final Player victim;

    /** the amount of damage that was inflicted because of this */
    private final double dealtDamage;

    /** the semi-friendly cause of this incident */
    private final DamageCause damageCause;

    public DamageEvent(Player victim, double dealtDamage, DamageCause damageCause)
    {
        this.victim = victim;
        this.dealtDamage = dealtDamage;
        this.damageCause = damageCause;
    }

    /**
     * @return who this happened to
     */
    public Player victim()
    {
        return victim;
    }

    /**
     * @return the total damage dealt
     */
    public double damageDealt()
    {
        return dealtDamage;
    }

    /**
     * @return the cause
     */
    public DamageCause damageCause()
    {
        return damageCause;
    }

    /**
     * @return what happened as text
     */
    public abstract BaseComponent[] information();

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

    /**
     * Bukkit
     *
     * @return {@link HandlerList}
     */
    @Override
    public HandlerList getHandlers()
    {
        return HANDLER_LIST;
    }

    /**
     * Bukkit
     *
     * @return {@link HandlerList}
     */
    public static HandlerList getHandlerList()
    {
        return HANDLER_LIST;
    }

}

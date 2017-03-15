package com.hyleria.coeus.damage;

import com.hyleria.util.HyleriaEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * @author Ben (OutdatedVersion)
 * @since 11:05 PM (May/07/2016)
 */
public class CombatDeathEvent extends HyleriaEvent
{

    private static final HandlerList HANDLER_LIST = new HandlerList();

    /** the log backing this system */
    private final DamageLog log;

    /** the final person attacked */
    private final Player finalAttacker;

    /** the player who died */
    private final Player victim;

    public CombatDeathEvent(Player who, Player lastDamageDealtBy, DamageLog log)
    {
        this.victim = who;
        this.log = log;
        this.finalAttacker = lastDamageDealtBy;
    }

    /**
     * @return the player who died
     */
    public Player victim()
    {
        return victim;
    }

    /**
     * @return the player this was dealt by
     */
    public Player by()
    {
        return finalAttacker;
    }

    /**
     * @return the log
     */
    public DamageLog log()
    {
        return log;
    }

    /** Bukkit */
    @Override
    public HandlerList getHandlers()
    {
        return HANDLER_LIST;
    }

    /** Bukkit */
    public static HandlerList getHandlerList()
    {
        return HANDLER_LIST;
    }
}

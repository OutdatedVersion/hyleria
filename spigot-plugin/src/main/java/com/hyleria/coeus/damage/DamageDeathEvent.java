package com.hyleria.coeus.damage;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Damage the player has taken
 *
 * @author Ben (OutdatedVersion)
 * @since 1:02 PM (May/23/2016)
 */
public class DamageDeathEvent extends PlayerEvent
{

    /** Bukkit */
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /** a way to manage what has happened to someone */
    private final DamageLog log;

    public DamageDeathEvent(Player who, DamageLog log)
    {
        super(who);

        this.log = log;
    }

    /**
     * @return the log for the player who this happened to
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

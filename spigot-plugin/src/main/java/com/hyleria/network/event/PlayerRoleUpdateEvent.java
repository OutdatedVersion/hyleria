package com.hyleria.network.event;

import com.hyleria.common.reference.Role;
import com.hyleria.util.HyleriaEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (9:00 PM)
 */
public class PlayerRoleUpdateEvent extends HyleriaEvent
{

    /** Bukkit */
    public static final HandlerList HANDLER_LIST = new HandlerList();

    /** player */
    private final Player player;

    /** previous role */
    private final Role previous;

    /** the new role */
    private final Role fresh;

    public PlayerRoleUpdateEvent(Player player, Role previous, Role fresh)
    {
        this.player = player;
        this.previous = previous;
        this.fresh = fresh;
    }

    /**
     * @return the player's who role changed
     */
    public Player player()
    {
        return player;
    }

    /**
     * @return the role of the player before the change
     */
    public Role previous()
    {
        return previous;
    }

    /**
     * @return the role the player now has
     */
    public Role fresh()
    {
        return fresh;
    }

    /** Bukkit */
    public HandlerList getHandlerList()
    {
        return HANDLER_LIST;
    }

    /** Bukkit */
    @Override
    public HandlerList getHandlers()
    {
        return HANDLER_LIST;
    }

}

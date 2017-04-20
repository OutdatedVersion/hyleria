package com.hyleria.coeus.event;

import com.hyleria.coeus.Status;
import com.hyleria.util.HyleriaEvent;
import org.bukkit.event.HandlerList;

/**
 * A notification event for letting
 * hooks know when the game's state
 * changes. No actions may be taken
 * to modify the result of said change.
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/14/2017 (11:42 PM)
 */
public class StatusChangeEvent extends HyleriaEvent
{

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final Status previous;
    private final Status next;

    public StatusChangeEvent(Status previous, Status next)
    {
        this.previous = previous;
        this.next = next;
    }

    /**
     * @return the status before the change
     */
    public Status previous()
    {
        return previous;
    }

    /**
     * @return the status after (now) the change
     */
    public Status next()
    {
        return next;
    }

    @Override
    public HandlerList getHandlers()
    {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList()
    {
        return HANDLER_LIST;
    }

}

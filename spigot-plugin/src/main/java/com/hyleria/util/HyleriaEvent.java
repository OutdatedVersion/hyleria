package com.hyleria.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/14/2017 (11:43 PM)
 */
public abstract class HyleriaEvent extends Event
{

    /**
     * Runs this event through Bukkit's event system
     */
    public HyleriaEvent call()
    {
        Bukkit.getServer().getPluginManager().callEvent(this);
        return this;
    }

    /**
     * @param clazz the type we're looking for
     * @return this event dirty casted to the
     *         provided class. (up to you to
     *         verify that this will work)
     */
    public <T> T call(Class<T> clazz)
    {
        call();
        return (T) this;
    }

}

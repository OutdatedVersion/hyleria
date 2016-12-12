package net.ultimateuhc.util;

import com.google.inject.AbstractModule;
import org.bukkit.event.Listener;

/**
 * OutdatedVersion
 * Dec/11/2016 (7:04 PM)
 */

public abstract class Module extends AbstractModule implements Listener
{

    /**
     *
     * @throws Exception
     */
    public abstract void initialize() throws Exception;

    @Override
    protected void configure()
    {

    }

}

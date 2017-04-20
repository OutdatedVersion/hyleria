package com.hyleria.coeus.available.uhc.scenario;

import org.bukkit.event.Listener;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (7:40 PM)
 */
public abstract class UHCScenario implements Listener
{

    /**
     * Invoked when this scenario is
     * started manually
     */
    public void init()
    {

    }

    /**
     * Called when this scenario is
     * disabled by someone manually
     */
    public void cleanUp()
    {

    }

    /**
     * @return the name of this scenario
     */
    public abstract String name();

}

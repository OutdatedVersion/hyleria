package com.hyleria.util;

import com.google.inject.Inject;
import com.hyleria.Hyleria;
import org.bukkit.event.Listener;


 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/11/2016 (7:04 PM)
  */
public abstract class Module implements Listener
{

    /** our plugin instance */
    @Inject private Hyleria plugin;

    /**
     * Guice delegate for this module (kind of)
     */
    public void configure()
    {
        plugin.registerListeners(this.getClass());
    }

    /**
     * Grabs an instance (or creates) from our injector
     *
     * @param clazz the type of class you're looking for
     * @param <T> a type parameter allowing us to return
     *            an instance of the class you're grabbing
     * @return the thing you wanted
     */
    <T> T require(Class<T> clazz)
    {
        return plugin.injector().getInstance(clazz);
    }

    /**
     * @return our plugin instance
     */
    Hyleria plugin()
    {
        return plugin;
    }

}

package com.hyleria.util;

import com.hyleria.Hyleria;
import org.bukkit.event.Listener;


 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/11/2016 (7:04 PM)
  */
public abstract class Module implements Listener
{

    /** our plugin instance */
    private Hyleria plugin;

    /**
     */
    public void configure(Hyleria plugin)
    {
        // should always call super
        this.plugin = plugin;
    }

    /**
     * Grabs an instance (or creates) from our injector
     *
     * @param clazz the type of class you're looking for
     * @param <T> a type parameter allowing us to return
     *            an instance of the class you're grabbing
     * @return the thing you wanted
     */
    public <T> T require(Class<T> clazz)
    {
        return plugin.injector().getInstance(clazz);
    }

    /**
     * @return our plugin instance
     */
    public Hyleria plugin()
    {
        return plugin;
    }

}

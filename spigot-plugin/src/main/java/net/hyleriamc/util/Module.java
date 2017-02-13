package net.hyleriamc.util;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import net.hyleriamc.Hyleria;
import org.bukkit.event.Listener;


 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/11/2016 (7:04 PM)
  */
public abstract class Module extends AbstractModule implements Listener
{

    /** our plugin instance */
    @Inject
    private Hyleria plugin;

    /**
     * Guice delegate for this module
     */
    @Override
    protected void configure()
    {
        System.out.println("[Debug] Hit #configure");

        plugin.registerListeners(this.getClass());
    }

    /**
     * Binds the particular object to our
     * injector so we may use it later.
     *
     * @param object the object
     */
    com.google.inject.Module use(Module object)
    {
        super.binder().install(object);
        return object;
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

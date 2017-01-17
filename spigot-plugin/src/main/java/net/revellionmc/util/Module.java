package net.revellionmc.util;

import com.google.inject.AbstractModule;
import net.revellionmc.Revellion;
import org.bukkit.event.Listener;

/**
 * OutdatedVersion
 * Dec/11/2016 (7:04 PM)
 */

public abstract class Module extends AbstractModule implements Listener
{

    /**
     * Guice delegate for this module
     */
    @Override
    protected void configure()
    {
        try
        {
            // register this to Bukkit's event system there's
            // actually a listener in this class

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.err.println("Issue starting module: " + getClass().getSimpleName());
        }
    }

    /**
     * Binds the particular object to our
     * injector so we may use it later.
     *
     * @param object the object
     */
    void use(Module object)
    {
        super.binder().install(object);
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
        return Revellion.get().injector().getInstance(clazz);
    }

}

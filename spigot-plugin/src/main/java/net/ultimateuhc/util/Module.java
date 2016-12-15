package net.ultimateuhc.util;

import com.google.inject.AbstractModule;
import net.ultimateuhc.UltimateUHC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.stream.Stream;

/**
 * OutdatedVersion
 * Dec/11/2016 (7:04 PM)
 */

public abstract class Module extends AbstractModule implements Listener
{

    /**
     * In charge of starting this
     * particular module
     *
     * @throws Exception in case something goes wrong
     */
    public abstract void initialize() throws Exception;

    /**
     * Guide delegate for our forcefully present
     * {@link #initialize()} method.
     */
    @Override
    protected void configure()
    {
        try
        {
            initialize();

            // let's automate a few things
            if (Stream.of(this.getClass().getMethods()).filter(ann -> ann.isAnnotationPresent(EventHandler.class)).count() >= 1)
                Bukkit.getServer().getPluginManager().registerEvents(this, require(UltimateUHC.class));
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
    public void use(Module object)
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
    public <T> T require(Class<T> clazz)
    {
        return UltimateUHC.get().injector().getInstance(clazz);
    }

}

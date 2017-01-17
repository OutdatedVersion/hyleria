package net.revellionmc;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.revellionmc.network.database.Database;
import net.revellionmc.util.ShutdownHook;
import net.revellionmc.util.time.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;


/**
 * Handles the setup & initialization
 * of this plugin
 *
 * @author Ben (OutdatedVersion)
 * @since Dec/11/2016 (5:48 PM)
 */
public class Revellion extends JavaPlugin
{

    private Injector injector;
    private List<Method> shutdownHooks;

    @Override
    public void onEnable()
    {
        shutdownHooks = Lists.newArrayList();


        // our primary injector - what we base a whole
        // lot of this plugin around
        injector = Guice.createInjector(binder ->
        {
            // this plugin
            binder.bind(Revellion.class).toInstance(this);
            binder.bind(Server.class).toInstance(Bukkit.getServer());
            binder.bind(BukkitScheduler.class).toInstance(Bukkit.getServer().getScheduler());

            // a few things that we'll just start on our own..
            binder.bind(Database.class).toInstance(new Database("production.json"));
            binder.bind(Timer.class).toInstance(new Timer());
        });
    }

    @Override
    public void onDisable()
    {
        System.out.println("Executing shutdown hooks..");

        shutdownHooks.forEach(method ->
        {
            try
            {
                method.invoke(this);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                System.err.println("\nIssue handling the executing of a shutdown hook");
                System.err.println("Origin: [" + method.getDeclaringClass().getSimpleName() + "]");
            }
        });
    }

    /**
     * @return grabs the one & only instance
     *         of our plugin for this server
     */
    public static Revellion get()
    {
        return JavaPlugin.getPlugin(Revellion.class);
    }

    /**
     * @return the injector bound to this plugin
     */
    public Injector injector()
    {
        return injector;
    }

    /**
     * Scans through the provided {@link Class}
     * and collects all of the methods annotated
     * with {@link ShutdownHook}. These methods
     * are to be executed when the plugin (in
     * most cases the server will be turning
     * off when this occurs) disables. Invalid
     * shutdown hooks, i.e. ones with any
     * non-zero number of parameters
     * will silently fail.
     *
     * @param clazz the class to look through
     * @return this plugin
     */
    public Revellion registerHook(Class<?> clazz)
    {
        // consider satisfying parameters with our injector?

        Stream.of(clazz.getMethods())
              .filter(method -> method.isAnnotationPresent(ShutdownHook.class))
              .filter(method -> method.getParameterCount() == 0)
              .forEach(shutdownHooks::add);

        return this;
    }

    /**
     * Lets Bukkit's event system know about
     * a listener bound to a certain class.
     *
     * <br>
     * We do verify that the required {@link EventHandler}
     * annotation is included in that class somewhere.
     *
     * <br>
     * In the case that it is we'll hit up our injector
     * to create a new instance of that item.
     *
     * @param classSet the things we'd like to register
     * @return our plugin instance
     */
    public Revellion registerListener(Class<? extends Listener>... classSet)
    {
        Stream.of(classSet)
                .filter(ann -> ann.isAnnotationPresent(EventHandler.class))
                .forEach(clazz -> Bukkit.getServer().getPluginManager().registerEvents(injector.getInstance(clazz), this));

        return this;
    }

}

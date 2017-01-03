package net.revellionmc;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.revellionmc.network.database.Database;
import net.revellionmc.util.ShutdownHook;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.List;

/**
 * OutdatedVersion
 * Dec/11/2016 (5:48 PM)
 */

public class Revellion extends JavaPlugin
{

    private Reflections reflections;
    private Injector injector;
    private List<Method> shutdownHooks;

    @Override
    public void onEnable()
    {
        reflections = new Reflections("net.revellionmc");

        // collect all of our hooks (silently fail on invalid ones)
        shutdownHooks = Lists.newArrayList();

        reflections.getMethodsAnnotatedWith(ShutdownHook.class)
                   .stream()
                   .filter(method -> method.getParameterCount() == 0)
                   .forEach(shutdownHooks::add);

        System.out.printf("Found %s shutdown hook%s\n", shutdownHooks.size(), shutdownHooks.size() >= 2 ? "s" : "");


        // our primary injector - what we base a whole
        //  lot of this plugin around
        injector = Guice.createInjector(binder ->
        {
            // this plugin
            binder.bind(Revellion.class).toInstance(this);
            binder.bind(Server.class).toInstance(Bukkit.getServer());
            binder.bind(BukkitScheduler.class).toInstance(Bukkit.getServer().getScheduler());

            // a few things that we'll just start on our own..
            binder.bind(Database.class).toInstance(new Database("production.json"));
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
                System.err.println();
                System.err.println("Issue handling the executing of a shutdown hook");
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

}

package net.ultimateuhc;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.ultimateuhc.network.AccountHandler;
import net.ultimateuhc.network.database.Database;
import net.ultimateuhc.util.ShutdownHook;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.List;

/**
 * OutdatedVersion
 * Dec/11/2016 (5:48 PM)
 */

public class UltimateUHC extends JavaPlugin
{

    private static UltimateUHC Instance;

    private Reflections reflections;
    private Injector injector;
    private List<Method> shutdownHooks;

    @Override
    public void onEnable()
    {
        Instance = this;

        reflections = new Reflections("net.ultimateuhc");

        // collect all of our hooks (silently fail on invalid ones)
        shutdownHooks = Lists.newArrayList();

        reflections.getMethodsAnnotatedWith(ShutdownHook.class)
                   .stream()
                   .filter(method -> method.getParameterCount() == 0)
                   .forEach(shutdownHooks::add);

        System.out.printf("Found %s shutdown hook%s\n", shutdownHooks.size(), shutdownHooks.size() >= 2 ? "s" : "");


        // our primary injector - what we base a whole lot of this
        // plugin around
        injector = Guice.createInjector(binder ->
        {
            // this plugin
            binder.bind(UltimateUHC.class).toInstance(this);

            // a few things that we'll just start on our own..
            binder.bind(Database.class).toInstance(new Database("production.json"));
            binder.bind(AccountHandler.class).toInstance(new AccountHandler());
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
            }
        });
    }

    /**
     * @return grabs the one & only instance
     *         of our plugin for this server
     */
    public static UltimateUHC get()
    {
        return Instance;
    }

    /**
     * @return the injector bound to this plugin
     */
    public Injector injector()
    {
        return injector;
    }

}

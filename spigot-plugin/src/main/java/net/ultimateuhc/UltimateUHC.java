package net.ultimateuhc;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.ultimateuhc.module.Chat;
import net.ultimateuhc.network.AccountHandler;
import net.ultimateuhc.network.database.Database;
import net.ultimateuhc.util.Module;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * OutdatedVersion
 * Dec/11/2016 (5:48 PM)
 */

public class UltimateUHC extends JavaPlugin
{

    private static UltimateUHC Instance;

    private Injector injector;
    private List<Method> shutdownHooks;

    private List<Class<? extends Module>> modules = Arrays.asList(AccountHandler.class, Chat.class);

    @Override
    public void onEnable()
    {
        Instance = this;

        // queue modules to be bound

        // check if the module has a ShutdownHook - if present: add to list
        shutdownHooks = Lists.newArrayList();

        final com.google.inject.Module _mainModule = binder ->
        {
            binder.bind(UltimateUHC.class).toInstance(Instance);

            binder.bind(Database.class).toInstance(new Database("database/production.json"));
        };

        injector = Guice.createInjector(_mainModule);
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

package com.hyleria;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hyleria.command.api.CommandHandler;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.common.inject.Requires;
import com.hyleria.common.inject.StartParallel;
import com.hyleria.common.redis.RedisChannel;
import com.hyleria.common.redis.RedisHandler;
import com.hyleria.common.reference.Constants;
import com.hyleria.util.Module;
import com.hyleria.util.ShutdownHook;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * Handles the setup & initialization
 * of the Bukkit side Hyleria.
 *
 * @author Ben (OutdatedVersion)
 * @since Dec/11/2016 (5:48 PM)
 */
public class Hyleria extends JavaPlugin
{

    /** our injector */
    private Injector injector;

    /** a collection of our shutdown hooks */
    private List<Method> shutdownHooks;

    @Override
    public void onEnable()
    {
        shutdownHooks = Lists.newArrayList();

        // our primary injector - what we base a whole
        // lot of this plugin around
        injector = Guice.createInjector(Constants.ENV, binder ->
        {
            // this plugin
            binder.bind(Hyleria.class).toInstance(this);
            binder.bind(Server.class).toInstance(Bukkit.getServer());
            binder.bind(BukkitScheduler.class).toInstance(Bukkit.getServer().getScheduler());
            binder.bind(WorldEditPlugin.class).toInstance(JavaPlugin.getPlugin(WorldEditPlugin.class));

            try
            {
                binder.bind(ServerConfig.class).toInstance(new Gson().fromJson(FileUtils.readFileToString(new File(ServerConfig.FILE_NAME)), ServerConfig.class));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.err.println("\nIssue reading primary server configuration file");
            }
        });

        // connect to Redis instance
        inject(RedisHandler.class).init().subscribe(RedisChannel.DEFAULT);

        // setup our command handler
        final CommandHandler _commandService = inject(CommandHandler.class).addProviders(CommandHandler.DEFAULT_PROVIDERS);


        // automatic creation of modules
        System.out.println("Class path scanner filter: [" + getClass().getPackage().getName() + ".*]");

        final List<Class<?>> _toLoad = Lists.newArrayList();

        new FastClasspathScanner(getClass().getPackage().getName())
                .matchClassesWithAnnotation(StartParallel.class, _toLoad::add)
                .matchClassesWithMethodAnnotation(ShutdownHook.class, (clazz, method) -> shutdownHooks.add(method)).scan();

        // verify that we're creating these in the right order
        // well, this may not be properly required. Guice should order things
        // in the order we need them for creation. We'll keep this instead we
        // do actually have some underlying need for the order.
        _toLoad.sort((one, two) ->
        {
            if (two.isAnnotationPresent(Requires.class))
                if (Stream.of(two.getAnnotation(Requires.class).value()).anyMatch(each -> Objects.equals(one, each)))
                    return -1;

            return 0;
        });

        _toLoad.forEach(this::inject);

        // let's register our commands now
        _commandService.registerInPackage("com.hyleria.command");
    }

    @Override
    public void onDisable()
    {
        System.out.println("Executing shutdown hooks..");

        shutdownHooks.forEach(method ->
        {
            try
            {
                method.invoke(injector.getInstance(method.getDeclaringClass()));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                System.err.println("\nIssue handling the executing of a shutdown hook");
                System.err.println("Origin: [" + method.getDeclaringClass().getName() + "]");
            }
        });
    }

    /**
     * @return grabs the one & only instance
     *         of our plugin for this server
     */
    public static Hyleria get()
    {
        return JavaPlugin.getPlugin(Hyleria.class);
    }

    /**
     * @return the injector bound to this plugin
     */
    public Injector injector()
    {
        return injector;
    }

    /**
     * Helpful little shortcut
     *
     * @param clazz class of what we're looking for
     * @return an instance of the provided
     *         class satisfied by {@link Guice}
     */
    public <T> T get(Class<T> clazz)
    {
        return injector.getInstance(clazz);
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
    public Hyleria registerHook(Class<?> clazz)
    {
        // consider satisfying parameters with our injector?

        Stream.of(clazz.getMethods())
              .filter(method -> method.isAnnotationPresent(ShutdownHook.class))
              .filter(method -> method.getParameterCount() == 0)
              .forEach(shutdownHooks::add);

        return this;
    }

    /**
     * Registers the provided (non-null)
     * listeners to Bukkit's event system.
     *
     * @param listeners the listeners
     * @return this plugin
     */
    public Hyleria registerListeners(Listener... listeners)
    {
        for (Listener listener : listeners)
            if (listener != null)
                Bukkit.getPluginManager().registerEvents(listener, this);

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
    public Hyleria registerListeners(Class<? extends Listener>... classSet)
    {
        Stream.of(classSet)
                .filter(clazz -> Stream.of(clazz.getMethods()).anyMatch(method -> method.isAnnotationPresent(EventHandler.class)))
                .forEach(clazz -> Bukkit.getServer().getPluginManager().registerEvents(injector.getInstance(clazz), this));

        return this;
    }

    /**
     * @param clazz the class we're working with
     * @param <T> a type parameter for the type
     *            of the provided class
     */
    public <T> T inject(Class<T> clazz)
    {
        T _instance = injector.getInstance(clazz);

        // invoke configuration of modules
        if (_instance instanceof Module)
            ((Module) _instance).configure(this);

        // auto register listeners
        if (_instance instanceof Listener)
            Bukkit.getPluginManager().registerEvents(((Listener) _instance), this);

        return _instance;
    }

}

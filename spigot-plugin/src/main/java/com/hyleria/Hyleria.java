package com.hyleria;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hyleria.coeus.Coeus;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.common.inject.ConfigurationProvider;
import com.hyleria.common.inject.Requires;
import com.hyleria.common.inject.StartParallel;
import com.hyleria.common.reference.Constants;
import com.hyleria.util.Module;
import com.hyleria.util.ShutdownHook;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * Handles the setup & initialization
 * of this plugin
 *
 * @author Ben (OutdatedVersion)
 * @since Dec/11/2016 (5:48 PM)
 */
public class Hyleria extends JavaPlugin
{

    private Injector injector;
    private List<Method> shutdownHooks;

    @Override
    public void onEnable()
    {
        shutdownHooks = Lists.newArrayList();
        // TODO(Ben): handle shutdown hooks

        // our primary injector - what we base a whole
        // lot of this plugin around
        injector = Guice.createInjector(Constants.ENV, binder ->
        {
            // this plugin
            binder.bind(Hyleria.class).toInstance(this);
            binder.bind(Server.class).toInstance(Bukkit.getServer());
            binder.bind(BukkitScheduler.class).toInstance(Bukkit.getServer().getScheduler());
            binder.bind(ServerConfig.class).toInstance(new ConfigurationProvider().read(ServerConfig.FILE_NAME, ServerConfig.class));
        });


        System.out.println("Class path scanner filter: [" + getClass().getPackage().getName() + ".*]");

        final List<Class<?>> _toLoad = Lists.newArrayList();

        new FastClasspathScanner(getClass().getPackage().getName())
                .matchClassesWithAnnotation(StartParallel.class, _toLoad::add).scan();

        // verify that we're creating these in the right order
        _toLoad.sort((one, two) ->
        {
            if (two.isAnnotationPresent(Requires.class))
                if (Stream.of(two.getAnnotation(Requires.class).value()).anyMatch(each -> Objects.equals(one, each)))
                    return -1;

            return 0;
        });

        _toLoad.forEach(this::boundInjection);


        // I want to guarantee this will load last
        boundInjection(Coeus.class);
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
    public <T> void boundInjection(Class<T> clazz)
    {
        T _instance = injector.getInstance(clazz);

        if (_instance instanceof Module)
            ((Module) _instance).configure();
    }

}

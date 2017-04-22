package com.hyleria.bungee;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hyleria.bungee.handle.Ping;
import com.hyleria.bungee.network.RequestProcessor;
import com.hyleria.common.redis.RedisChannel;
import com.hyleria.common.redis.RedisHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.stream.Stream;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/25/2017 (12:13 PM)
 */
@SuppressWarnings ( "unchecked" )
public class Hyleria extends Plugin
{

    /** injector for this plugin */
    private Injector injector;

    @Override
    public void onEnable()
    {
        injector = Guice.createInjector(binder ->
        {
            binder.bind(Hyleria.class).toInstance(this);
            binder.bind(ProxyServer.class).toInstance(ProxyServer.getInstance());
        });

        injector.getInstance(RedisHandler.class).init().subscribe(RedisChannel.NETWORK);

        inject(RequestProcessor.class);
        injectAndRegister(Ping.class);
    }

    @Override
    public void onDisable()
    {
        injector.getInstance(RedisHandler.class).releaseResources();
    }

    /**
     * @param classes the classes to inject
     */
    private void inject(Class... classes)
    {
        for (Class clazz : classes)
            injector.getInstance(clazz);
    }

    /**
     * For each one of the provided classes
     * create a new instance of it, and register
     * it to this proxy's event system.
     *
     * @param classes the classes to do so with
     */
    private void injectAndRegister(Class<? extends Listener>... classes)
    {
        Stream.of(classes).forEach(clazz -> getProxy().getPluginManager()
                    .registerListener(this, injector.getInstance(clazz)));
    }

}

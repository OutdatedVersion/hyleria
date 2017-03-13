package com.hyleria.backend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hyleria.commons.inject.StartParallel;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (4:43 PM)
 */
public class NetworkManager
{

    private Injector injector;

    // track servers
    // create servers
    // analytics at some point

    public NetworkManager()
    {
        injector = Guice.createInjector(binder -> binder.bind(NetworkManager.class).toInstance(this));

        new FastClasspathScanner(getClass().getPackage().getName())
                .matchClassesWithAnnotation(StartParallel.class, injector::getInstance).scan();
    }

}

package com.hyleria.coeus;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.util.Module;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/08/2017 (5:40 PM)
 */
@Singleton
public class Coeus extends Module
{

    @Inject private Injector injector;

    @Inject
    public Coeus(ServerConfig config)
    {
        // manage match cycles
        //

        final Game _game = injector.getInstance(GameChoice.valueOf(config.forcedGame).clazz);
    }

}

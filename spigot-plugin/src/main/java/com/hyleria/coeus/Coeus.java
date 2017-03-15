package com.hyleria.coeus;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.hyleria.coeus.event.StatusChangeEvent;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.util.Module;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/08/2017 (5:40 PM)
 */
@Singleton
public class Coeus extends Module
{

    /** local injector */
    @Inject private Injector injector;

    /** the type of the game we're playing */
    private GameChoice gameChoice;

    /** the only game that's running */
    private Game game;

    /** the status of the game */
    private Status status;

    @Inject
    public Coeus(ServerConfig config)
    {
        status = Status.INIT;  // we don't need to call the event quite yet
        gameChoice = GameChoice.valueOf(config.forcedGame);

        game = injector.getInstance(gameChoice.clazz);
    }

    /**
     * Updates the status of the current
     * match to the provided status. We
     * also send out a notification-only
     * event to let us interact with it.
     *
     * @param to the new status
     * @return this
     */
    public Coeus updateStatus(Status to)
    {
        new StatusChangeEvent(status, to).call();

        this.status = to;

        return this;
    }

}

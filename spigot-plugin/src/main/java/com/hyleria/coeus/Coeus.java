package com.hyleria.coeus;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.coeus.damage.DamageEventFactory;
import com.hyleria.coeus.event.StatusChangeEvent;
import com.hyleria.coeus.scoreboard.ScoreboardHandler;
import com.hyleria.common.backend.ServerConfig;
import com.hyleria.network.AccountManager;
import com.hyleria.util.Module;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.stream.Stream;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/08/2017 (5:40 PM)
 */
@Singleton
public class Coeus extends Module
{

    /** our plugin */
    public final Hyleria plugin;

    /** the type of the game we're playing */
    private GameChoice gameChoice;

    /** the only game that's running */
    private Game game;

    /** the status of the game */
    private Status status;

    @Inject
    public Coeus(Hyleria plugin, ServerConfig config, AccountManager accountManager)
    {
        this.plugin = plugin;

        status = Status.INIT;  // we don't need to call the event quite yet

        gameChoice = GameChoice.valueOf(config.forcedGame.toUpperCase());
        game = plugin.boundInjection(gameChoice.clazz);

        // load up the basic game
        game.init(this);

        // ready for players
        // the above operation is intended to be fully blocking
        updateStatus(Status.IDLE);
        plugin.registerListeners(this);


        new DamageEventFactory(game).init(plugin);
        new ScoreboardHandler().game(game).init(plugin, this).title(game.scoreboardTitle)
                          .initNametags(plugin, accountManager);
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

    /**
     * @return the status of the current game
     */
    public Status status()
    {
        return status;
    }

    /**
     * @param statuses all of the statuses we're checking for
     * @return whether or not it matches
     */
    public boolean isStatus(Status... statuses)
    {
        return Stream.of(statuses).anyMatch(s -> s == this.status);
    }

    /**
     * @return whether or not our current game
     *         is actually active
     */
    public boolean isRunning()
    {
        return status == Status.ACTIVE_GAME;
    }

    /**
     * @return the current game
     */
    public Game game()
    {
        return game;
    }

    /**
     * @return the name of the game we're playing
     */
    public String gameName()
    {
        // may need to do this differently in the future..?
        return TextUtil.formatEnum(this.gameChoice);
    }

    /**
     * @return the engine
     */
    public Coeus hookGameListener()
    {
        plugin.registerListeners(game);
        return this;
    }

    /**
     * @return the engine
     */
    public Coeus unhookGameListener()
    {
        HandlerList.unregisterAll(game);
        return this;
    }

    /**
     * Start our game
     *
     * @return the current game
     */
    public Game startGame()
    {
        updateStatus(Status.PRE_GAME);

        game.begin();
        plugin.registerListeners(game);

        return game;
    }

    @EventHandler
    public void watchJoin(PlayerJoinEvent event)
    {
        if (status == Status.IDLE)
        {
            if (game.preGameJoinHandler != null)
                game.preGameJoinHandler.accept(event);


            // we allow the ability to disable this
            if (game.requiredPlayerCount != -1)
            {
                if (PlayerUtil.onlineCount() >= game.requiredPlayerCount)
                {
                    // updateStatus(Status.LOBBY_COUNTDOWN);
                    // for now we'll just start it
                    // TODO(Ben): actually have a countdown for other games

                    startGame();
                }
            }
        }
    }

    @EventHandler ( priority = EventPriority.HIGH )
    public void watchLogin(AsyncPlayerPreLoginEvent event)
    {
        if (status == Status.INIT || status == Status.MAP_FETCH)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "This server is still starting up.. give us a bit longer.");
    }

}

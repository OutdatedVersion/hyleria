package com.hyleria.coeus;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.hyleria.scoreboard.PlayerScoreboard;
import com.hyleria.common.json.GSONUtil;
import com.hyleria.util.GameFlagHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (5:43 PM)
 */
@Singleton
public abstract class Game implements Listener
{

    /** shared {@link Gson} instance for games */
    private static final Gson GSON = new Gson();

    /**  */
    public Consumer<PlayerJoinEvent> preGameJoinHandler;

    /**  */
    public GameFlagHandler<PlayerJoinEvent> joinHandler = GameFlagHandler.disabled(PlayerJoinEvent.class);

    /** */
    public GameFlagHandler<PlayerLoginEvent> loginHandler = GameFlagHandler.disabled(PlayerLoginEvent.class);

    /** if we should let people know someone died */
    public boolean announceDeath = true;

    /** a way to handle deaths in a special way per game */
    public Consumer<PlayerDeathEvent> deathHandler;

    /** when someone dies we'll send them the hearts of their killer (if it's a player) */
    public boolean showKillerHeartsOnDeath = true;

    /** the total number of players we need to start the game */
    public int requiredPlayerCount = 2;

    /** the title of the scoreboard */
    public String scoreboardTitle = "Hyleria";

    /** TEMPORARY SOLUTION. to be replaced by full stat tracking system. */
    public Multimap<UUID, String> kills = MultimapBuilder.hashKeys().arrayListValues().build();

    /**
     * Invoked by the engine when the
     * server is first started. Until
     * this operation completes players
     * will not be accepted.
     *
     * Should contain semi time-consuming
     * logic
     *
     * @param engine our game engine
     */
    public abstract void init(Coeus engine);

    /**
     * Invoked when we have the required
     * players to {@code begin} the game.
     * Should contain game logic.
     */
    public abstract void begin();

    /**
     * Called when a match is over and the
     * server will be shutting down within
     * a short time period.
     */
    public abstract void end();

    /**
     * Update the scoreboard within matches
     *
     * @param scoreboard the scoreboard
     */
    public void updateScoreboard(PlayerScoreboard scoreboard)
    {

    }

    /**
     * Loads a game config that is stored
     * in the file named as provided.
     *
     * @param name the name of the configuration
     * @param clazz the class of the config
     * @param <T> type of the configuration being loaded
     *
     * @return the configuration
     */
    protected <T> T loadConfig(String name, Class<T> clazz)
    {
        return GSONUtil.fromFile(name + "_data.json", clazz, GSON);
    }

    /**
     * We may need our game as something else;
     * this allows us to do that.
     *
     * @param clazz the type we want to be using
     * @param <T> type parameter
     *
     * @return an instance of that class
     */
    public <T> T as(Class<T> clazz)
    {
       return clazz.cast(this);
    }

}

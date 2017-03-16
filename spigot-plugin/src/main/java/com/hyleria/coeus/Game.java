package com.hyleria.coeus;

import com.google.gson.Gson;
import com.hyleria.coeus.scoreboard.PlayerScoreboard;
import com.hyleria.common.json.GSONUtil;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.function.Consumer;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (5:43 PM)
 */
public abstract class Game
{

    /** shared {@link Gson} instance for games */
    private static final Gson GSON = new Gson();

    /** if we should let people know someone died */
    public boolean announceDeath;

    /** a way to handle deaths in a special way per game */
    public Consumer<PlayerDeathEvent> deathHandler;

    /** when someone dies we'll send them the hearts of their killer (if it's a player) */
    public boolean showKillerHeartsOnDeath;

    /** the total number of players we need to start the game */
    public int requiredPlayerCount = 2;

    /**
     * Invoked by the engine when the
     * server is first started. Until
     * this operation completes players
     * will not be accepted.
     *
     * Should contain semi time-consuming
     * logic
     */
    public abstract void init();

    /**
     * Invoked when we have the required
     * players to {@code begin} the game.
     * Should contain game logic.
     */
    public abstract void begin();

    /**
     * Called when a match is over & the
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
     * @return an instance of that class
     */
    protected <T> T selfAs(Class<T> clazz)
    {
       return clazz.cast(this);
    }

}

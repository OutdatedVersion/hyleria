package com.hyleria.scoreboard;

import com.google.common.collect.Maps;
import com.hyleria.Hyleria;
import com.hyleria.module.Nametags;
import com.hyleria.network.AccountManager;
import com.hyleria.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/16/2017 (12:24 AM)
 */
public class ScoreboardHandler implements Listener, Runnable
{

    /** the ID of the task running this board */
    private int taskID;

    /** what works through our scoreboard */
    private Consumer<PlayerScoreboard> processor;

    /** the title of all scoreboards created */
    private String title;

    /** the board of each player */
    private Map<Player, PlayerScoreboard> boardRelation;

    /**
     * Starts up this scoreboard manager
     *
     * @param plugin our plugin
     * @return a fresh handler
     */
    public ScoreboardHandler init(Hyleria plugin)
    {
        boardRelation = Maps.newHashMap();

        taskID = Scheduler.timer(this, 1);
        Scheduler.timerExact(new AnimationTask(), 4);

        plugin.registerListeners(this);

        return this;
    }

    /**
     * Updates the action for our handler. In theory
     * it's what puts the text required in place.
     *
     * @param consumer what handles this
     * @return this handler
     */
    public ScoreboardHandler processor(Consumer<PlayerScoreboard> consumer)
    {
        this.processor = consumer;
        return this;
    }

    /**
     * Updates the title on every board
     *
     * @param title the title
     * @return this handler
     */
    public ScoreboardHandler title(String title)
    {
        this.title = title;
        boardRelation.values().forEach(board -> board.title(title));
        return this;
    }

    /**
     * Start our nametags module
     *
     * @param plugin it needs this
     * @param accountManager it needs this
     * @return the new module
     */
    public Nametags initNametags(Hyleria plugin, AccountManager accountManager)
    {
        return new Nametags(plugin, accountManager, this);
    }

    @Override
    public void run()
    {
        if (processor == null)
            return;

        boardRelation.forEach((player, board) ->
        {
            // purge & draw are called every time (at least for now)

            board.purge();
            processor.accept(board);
            board.writeURL();
            board.draw();
        });
    }

    /**
     * Inserts the player's board into
     * our map, and set it on the player.
     *
     * @param player the player
     * @param scoreboard the board
     */
    public void updateBoard(Player player, PlayerScoreboard scoreboard)
    {
        boardRelation.put(player, scoreboard);
        player.setScoreboard(scoreboard.bukkitScoreboard());
    }

    /**
     * Creates a new scoreboard for the
     * provided player
     *
     * @param player the player
     */
    public void createBoard(Player player)
    {
        updateBoard(player, new PlayerScoreboard(player).title(title));
    }

    /**
     * Reset a scoreboard for the
     * provided player
     *
     * @param player the player
     */
    public void removeBoard(Player player)
    {
        final Scoreboard _board = boardRelation.remove(player).bukkitScoreboard();

        _board.getObjectives().forEach(Objective::unregister);
        _board.getTeams().forEach(Team::unregister);

        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    /**
     * @param player the player whom's board we're looking for
     * @return the board of the player
     */
    public PlayerScoreboard boardFor(Player player)
    {
        return boardRelation.get(player);
    }

    @EventHandler
    public void create(PlayerJoinEvent event)
    {
        createBoard(event.getPlayer());
    }

    @EventHandler
    public void cleanup(PlayerQuitEvent event)
    {
        removeBoard(event.getPlayer());
    }

    /**
     * Update the title of scoreboards so they're all fancy & stuff
     */
    private class AnimationTask implements Runnable
    {
        @Override
        public void run()
        {
            boardRelation.values().forEach(PlayerScoreboard::animationTick);
        }
    }

}

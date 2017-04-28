package com.hyleria.lobby;

import com.hyleria.Hyleria;
import com.hyleria.api.NoEngine;
import com.hyleria.common.time.Time;
import com.hyleria.network.AccountManager;
import com.hyleria.scoreboard.PlayerScoreboard;
import com.hyleria.scoreboard.ScoreboardHandler;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author Ben (OutdatedVersion)
 * @since Apr/28/2017 (9:14 AM)
 */
@NoEngine
public class Lobby extends JavaPlugin implements Listener, Consumer<PlayerScoreboard>
{

    /** how we pretty up the date on the scoreboard */
    private static final DateFormat FORMAT = new SimpleDateFormat("EEE MMM/dd");

    /** where players go when joining */
    private Location spawnPoint = new Location(Bukkit.getWorld("main_lobby"), 0.5, 21, 0.5, 47.8f, 5.1f);

    /** what we write on the scoreboard */
    private String date = updateDate();

    @Override
    public void onEnable()
    {
        /* our parent plugin */
        final Hyleria _plugin = JavaPlugin.getPlugin(Hyleria.class).registerListeners(this);

        // setup our scoreboard
        _plugin.get(ScoreboardHandler.class).init(_plugin).processor(this).enableNametags(_plugin, _plugin.get(AccountManager.class));

        // make sure our date changes every so often
        Scheduler.timerExact(this::updateDate, Time.MINUTES.toTicks(2));
    }

    @Override
    public void accept(PlayerScoreboard board)
    {
        board.writeHead("You");
        board.write(board.player().getName());
        board.blank().writeHead("Today");
        board.write(this.date);
        board.blank().writeHead("Players");
        board.write(PlayerUtil.onlineCount());
    }

    /**
     * Updates the date for this
     * lobby's scoreboard
     */
    private String updateDate()
    {
        return this.date = FORMAT.format(new Date());
    }

    @EventHandler
    public void keepOutOfVoid(EntityDamageEvent event)
    {
        // giant glass borders are (aesthetically) dumb

        if (event.getEntityType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.VOID)
        {
            event.setCancelled(true);
            event.getEntity().teleport(this.spawnPoint);
        }
    }

    @EventHandler
    public void teleport(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);

        event.getPlayer().teleport(spawnPoint);
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void prettyLeave(PlayerQuitEvent event)
    {
        event.setQuitMessage(null);
    }

}

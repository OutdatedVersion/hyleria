package com.hyleria.coeus.available.lobby;

import com.hyleria.coeus.Coeus;
import com.hyleria.coeus.Game;
import com.hyleria.coeus.Status;
import com.hyleria.coeus.damage.kinds.PlayerVoidDamageEvent;
import com.hyleria.coeus.scoreboard.PlayerScoreboard;
import com.hyleria.common.time.Time;
import com.hyleria.util.PlayerUtil;
import com.hyleria.util.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/18/2017 (7:26 PM)
 */
public class Lobby extends Game
{

    /** how we pretty up the date on the scoreboard */
    private static final DateFormat FORMAT = new SimpleDateFormat("EEE MMM/dd");

    /** what we write on the scoreboard */
    private String date = updateDate();

    /** where players spawn */
    private Location spawn = new Location(Bukkit.getWorld("main_lobby"), 0.5, 21, 0.5, 47.8f, 5.1f);

    @Override
    public void init(Coeus engine)
    {
        engine.updateStatus(Status.ACTIVE_GAME);
        engine.hookGameListener();

        Scheduler.timerExact(this::updateDate, Time.MINUTES.toTicks(2));
    }

    @Override
    public void begin()
    {

    }

    @Override
    public void end()
    {

    }

    @Override
    public void updateScoreboard(PlayerScoreboard scoreboard)
    {
        scoreboard.blank();
        scoreboard.writeHead("You");
        scoreboard.write(scoreboard.player().getName());
        scoreboard.blank();
        scoreboard.writeHead("Today");
        scoreboard.write(ChatColor.RED + this.date);
        scoreboard.blank();
        scoreboard.writeHead("Players");
        scoreboard.write(ChatColor.RED.toString() + PlayerUtil.onlineCount());
        scoreboard.writeURL();
        scoreboard.draw();
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
    public void keepOutOfVoid(PlayerVoidDamageEvent event)
    {
        // giant glass borders are (aesthetically) dumb

        event.setCancelled(true);
        event.victim().teleport(spawn);
    }

    @EventHandler
    public void teleport(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);

        event.getPlayer().teleport(spawn);
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void prettyLeave(PlayerQuitEvent event)
    {
        event.setQuitMessage(null);
    }

}

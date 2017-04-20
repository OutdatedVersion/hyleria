package com.hyleria.module;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.common.reference.Role;
import com.hyleria.network.AccountManager;
import com.hyleria.network.event.PlayerRoleUpdateEvent;
import com.hyleria.scoreboard.ScoreboardHandler;
import com.hyleria.util.Issues;
import com.hyleria.util.Module;
import com.hyleria.util.RoleFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/16/2017 (1:10 AM)
 */
@Singleton
public class Nametags extends Module
{

    /** allow access to account details */
    private final AccountManager accountManager;

    /** grab the player's board */
    private final ScoreboardHandler handler;

    @Inject
    public Nametags(Hyleria plugin, AccountManager accountManager, ScoreboardHandler handler)
    {
        this.accountManager = accountManager;
        this.handler = handler;

        plugin.registerListeners(this);
    }

    @EventHandler ( priority = EventPriority.HIGH )
    public void sendNametags(PlayerJoinEvent event)
    {
        refresh();
    }

    @EventHandler
    public void sendNametags(PlayerRoleUpdateEvent event)
    {
        refresh();
    }

    /**
     * Process player nametags
     */
    private void refresh()
    {
        try
        {
            for (Player scoreboardAll : Bukkit.getOnlinePlayers())
            {
                final Scoreboard _scoreboard = handler.boardFor(scoreboardAll).bukkitScoreboard();

                if (_scoreboard == null)
                    return;

                for (Player all : Bukkit.getOnlinePlayers())
                {
                    // Get the player's rank
                    Role _playerRole = accountManager.grab(all).role();

                    // The actual tag (prefix)
                    String _displayString;

                    if (_playerRole == Role.PLAYER)
                        _displayString = ChatColor.GRAY.toString();
                    else
                        _displayString = RoleFormat.chatFormat(_playerRole) + " ";


                    // Attempt to get a team
                    Team _correspondingTeam = _scoreboard.getTeam(_playerRole.name());

                    // Fallback if the team doesn't exist
                    if (_correspondingTeam == null)
                        _correspondingTeam = _scoreboard.registerNewTeam(_playerRole.name());

                    // Set the tag's prefix
                    _correspondingTeam.setPrefix(_displayString);

                    // Add the player
                    if (!_correspondingTeam.hasEntry(all.getName()))
                        _correspondingTeam.addEntry(all.getName());

                    // I'd update the list name as well, but 1.7 is pretty strict on that :/
                }
            }
        }
        catch (Exception e)
        {
            Issues.handle("Process Nametags", e);
        }
    }

}

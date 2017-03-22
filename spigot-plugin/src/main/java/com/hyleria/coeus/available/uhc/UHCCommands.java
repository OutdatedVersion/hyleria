package com.hyleria.coeus.available.uhc;

import com.google.inject.Inject;
import com.hyleria.coeus.Game;
import com.hyleria.command.api.Command;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.stream.Collectors;

import static com.hyleria.util.Colors.PLAYER;
import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.*;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (7:42 PM)
 */
public class UHCCommands
{

    /** the number of players to include in the top kill command */
    private static final int TOP_KILL_BOUND = 8;

    /** uhc game */
    @Inject private Game game;

    @Command ( executor = { "kc", "killcount", "kills" } )
    public void killCount(Player player, Player target)
    {
        int _amount = game.kills.get(target.getUniqueId()).size();

        player.sendMessage(bold(PLAYER) + target.getName() + bold(GRAY) + " has "
                            + _amount + bold(GRAY) + (_amount == 1 ? " kill." : " kills."));
    }

    @Command ( executor = { "kt", "topkills", "killtop" } )
    public void topPlayers(Player player)
    {
        // idk lol

        player.sendMessage(
                bold(DARK_AQUA) + "Top Kills this round" +
                game.kills.asMap().entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue().size()))
                .sorted(Comparator.comparingInt(Pair::getValue))
                .collect(Collectors.toList())
                .subList(0, TOP_KILL_BOUND - 1)
                .stream()
                .collect(StringBuilder::new, (builder, pair) ->
                {
                    final Player _player = Bukkit.getPlayer(pair.getKey());

                    if (_player != null)
                        builder.append(PLAYER).append(_player.getName())
                                .append(GRAY).append(" - ")
                                .append(RED).append(pair.getValue())
                                .append("\n");
                }, StringBuilder::append).toString());
    }

}

package com.hyleria.coeus.available.uhc;

import com.google.inject.Inject;
import com.hyleria.coeus.Coeus;
import com.hyleria.command.api.Command;
import com.hyleria.command.api.annotation.Permission;
import com.hyleria.common.inject.Requires;
import com.hyleria.common.reference.Role;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.hyleria.util.Colors.PLAYER;
import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.*;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (7:42 PM)
 */
@Requires ( Coeus.class )
public class UHCCommands
{

    /** the number of players to include in the top kill command | compensates for zero-position index */
    private static final int TOP_KILL_BOUND = 8 - 1;

    /** uhc game */
    @Inject private Coeus engine;

    @Command ( executor = { "kc", "killcount", "kills" } )
    public void killCount(Player player, Player target)
    {
        int _amount = engine.game().kills.get(target.getUniqueId()).size();

        // before our message build existed..
        player.sendMessage(bold(PLAYER) + target.getName() + bold(GRAY) + " has "
                            + _amount + bold(GRAY) + (_amount == 1 ? " kill." : " kills."));
    }

    @Command ( executor = { "kt", "topkills", "killtop" } )
    public void topPlayers(Player player)
    {
        // idk lol
        final List<Pair<UUID, Integer>> _sorted =
                engine.game().kills.asMap().entrySet()
                        .stream()
                        .map(entry -> Pair.of(entry.getKey(), entry.getValue().size()))
                        .sorted(Comparator.comparingInt(Pair::getValue))
                        .collect(Collectors.toList());

        player.sendMessage(
                bold(DARK_AQUA) + "Top Kills this round\n" +
                _sorted.subList(0, _sorted.size() < TOP_KILL_BOUND ? _sorted.size() : TOP_KILL_BOUND)
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

    @Command ( executor = "shrinkborder" )
    @Permission ( Role.ADMIN )
    public void shrink(Player player)
    {
        player.sendMessage(bold(YELLOW) + "Manually shrinking border..");
        engine.game().as(UHC.class).shrinkBorder();
    }

}

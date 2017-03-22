package com.hyleria.command.api.satisfier;

import com.hyleria.command.api.ArgumentSatisfier;
import com.hyleria.command.api.Arguments;
import com.hyleria.util.PlayerUtil;
import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (9:28 PM)
 */
public class PlayerSatisfier implements ArgumentSatisfier<Player>
{

    @Override
    public Player get(Player player, Arguments args)
    {
        return PlayerUtil.search(player, args.next(), true);
    }

    @Override
    public String fail(String provided)
    {
        return null;
    }

    @Override
    public Class<Player> satisfies()
    {
        return Player.class;
    }

}

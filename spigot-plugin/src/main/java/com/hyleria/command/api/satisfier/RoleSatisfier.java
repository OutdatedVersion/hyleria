package com.hyleria.command.api.satisfier;

import com.hyleria.command.api.ArgumentSatisfier;
import com.hyleria.command.api.Arguments;
import com.hyleria.common.reference.Role;
import org.bukkit.entity.Player;

import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (8:37 PM)
 */
public class RoleSatisfier implements ArgumentSatisfier<Role>
{

    @Override
    public Role get(Player player, Arguments args)
    {
        try
        {
            return Role.valueOf(args.next().toUpperCase());
        }
        catch (IllegalArgumentException ex)
        {
            return null;
        }
    }

    @Override
    public String fail(String provided)
    {
        return bold(GRAY) + "No role matching [" + bold(GREEN) + provided.toUpperCase() + bold(GRAY) + "].";
    }

    @Override
    public Class<Role> satisfies()
    {
        return Role.class;
    }

}

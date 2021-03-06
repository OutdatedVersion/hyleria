package com.hyleria.command.api.satisfier;

import com.hyleria.command.api.ArgumentSatisfier;
import com.hyleria.command.api.Arguments;
import org.bukkit.entity.Player;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (7:01 PM)
 */
public class StringArraySatisfier implements ArgumentSatisfier<String[]>
{

    @Override
    public String[] get(Player player, Arguments args)
    {
        final String[] _array = new String[args.remainingElements()];

        for (int i = args.currentPosition(); i < _array.length; i++)
        {
            _array[i] = args.next();
        }

        return _array;
    }

    @Override
    public String fail(String provided)
    {
        return "Unable to provision array... but how?";
    }

    @Override
    public Class<String[]> satisfies()
    {
        return String[].class;
    }

}

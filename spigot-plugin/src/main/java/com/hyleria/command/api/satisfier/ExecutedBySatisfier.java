package com.hyleria.command.api.satisfier;

import com.hyleria.command.api.AnnotatedArgumentSatisfier;
import com.hyleria.command.api.Arguments;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (11:33 AM)
 */
public class ExecutedBySatisfier implements AnnotatedArgumentSatisfier<Player>
{

    @Override
    public Class<? extends Annotation> requiresAnnotation()
    {
        return RanTheCommand.class;
    }

    @Override
    public Player get(Player player, Arguments args)
    {
        return ;
    }

}

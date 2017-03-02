package com.hyleria.command.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;
import com.hyleria.commons.reference.Role;

import java.lang.reflect.Method;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/28/2017 (5:50 PM)
 */
@Singleton
public class CommandHandler
{

    @Inject
    private Hyleria hyleria;

    public CommandHandler registerCommands(Object object)
    {
        for (Method method : object.getClass().getMethods())
        {
            if (method.isAnnotationPresent(Command.class))
            {
                final CommandInfo _info = new CommandInfo();


            }
        }

        return this;
    }

    static class CommandInfo
    {
        String[] executors;
        boolean commandArgsAsParameters;
        Role role;

        String node()
        {
            return "";
        }
    }

}

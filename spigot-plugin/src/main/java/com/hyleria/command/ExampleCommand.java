package com.hyleria.command;

import com.hyleria.command.api.Command;
import com.hyleria.command.api.RequiresRole;
import com.hyleria.commons.reference.Role;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/28/2017 (6:01 PM)
 */
public class ExampleCommand
{

    @Command ( executor = "role" )
    @RequiresRole ( Role.ADMIN )
    public void baseCommand()
    {

    }

}

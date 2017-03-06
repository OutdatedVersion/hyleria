package com.hyleria.command;

import com.hyleria.command.api.Command;
import com.hyleria.command.api.Permission;
import com.hyleria.commons.reference.Role;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/28/2017 (6:01 PM)
 */
public class ExampleCommand
{

    @Command ( executor = "role" )
    @Permission ( Role.ADMIN )
    public void baseCommand()
    {

    }

}

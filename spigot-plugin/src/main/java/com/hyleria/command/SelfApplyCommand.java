package com.hyleria.command;

import com.google.common.collect.Maps;
import com.hyleria.command.api.Command;
import com.hyleria.common.reference.Role;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/22/2017 (11:46 AM)
 */
public class SelfApplyCommand
{

    /** */
    private Map<UUID, Role> relation = Maps.newHashMap();

    @Command ( executor = "self", hidden = true )
    public void run(Player player)
    {

    }

}

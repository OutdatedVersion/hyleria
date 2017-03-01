package com.hyleria.command.api;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hyleria.Hyleria;

import java.util.Map;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/28/2017 (5:50 PM)
 */
@Singleton
public class CommandHandler
{

    @Inject
    private Hyleria hyleria;

    private Map<String, CommandData> commands = Maps.newHashMap();

    public static class CommandData
    {

    }

}

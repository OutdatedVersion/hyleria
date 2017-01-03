package net.revellionmc.util.time;

import com.google.common.collect.Maps;
import net.revellionmc.util.Module;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * OutdatedVersion
 * Dec/14/2016 (10:01 PM)
 */

public class Timer extends Module implements Runnable
{

    private final Map<Long, Method> timerTasks;
    private long lastCycle;

    public Timer()
    {
        timerTasks = Maps.newHashMap();


    }

    @Override
    public void run()
    {

    }

}


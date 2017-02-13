package net.hyleriamc.util.time;

import com.google.common.collect.Lists;
import net.hyleriamc.util.Module;
import net.hyleriamc.util.Scheduler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;


 /**
  * @author Ben (OutdatedVersion)
  * @since Dec/14/2016 (10:01 PM)
  */
// disable, for now - @StartParallelToServer
public class Timer extends Module implements Runnable
{

    private final List<TimerTask> timerTasks;

    public Timer()
    {
        timerTasks = Lists.newArrayList();

        Scheduler.timerExact(this, 1);
    }

    /**
     * Scans the provided
     *
     * @param clazz
     * @return
     */
    public Timer beginTracking(Class<?> clazz)
    {
        Stream.of(clazz.getMethods())
              .filter(method -> method.isAnnotationPresent(FireOn.class))
              .filter(method -> method.getParameterCount() == 0)
              .forEach(method ->
              {
                  final FireOn _data = method.getAnnotation(FireOn.class);


              });

        return this;
    }

    @Override
    public void run()
    {
        // skip process time if there's
        // nothing to work with
        if (timerTasks.isEmpty())
            return;

        timerTasks.forEach(task ->
        {
            if (task.amountOverride != 0)
            {

            }
            else
            {

            }
        });
    }

    static class TimerTask
    {
        int amountOverride;
        long millisecondsToHit;

        Method method;
    }

}


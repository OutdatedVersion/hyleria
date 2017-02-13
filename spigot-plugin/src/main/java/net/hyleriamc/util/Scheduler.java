package net.hyleriamc.util;

import net.hyleriamc.Hyleria;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Provides easy access to the time-based
 * methods of the Bukkit API.
 *
 * @author Ben (OutdatedVersion)
 * @since Jan/03/2017 (12:42 AM)
 */
public class Scheduler
{

    /**
     * @return the one and only scheduler instance for this server
     */
    public static BukkitScheduler get()
    {
        return Bukkit.getScheduler();
    }

    /**
     * Stops the requested task instantly
     *
     * @param id the ID of the task
     * @return the {@link BukkitScheduler} for this {@link org.bukkit.Server}
     */
    public static BukkitScheduler end(int id)
    {
        get().cancelTask(id);
        return get();
    }

    /**
     * Ends the task with the provided ID after
     * a certain amount of time. (measured in ticks)
     *
     * @param id the ID of the task
     * @param ticksLater how many ticks later to run something
     * @return the {@link BukkitScheduler} instance this server is using
     */
    public static BukkitScheduler endAfter(int id, long ticksLater)
    {
        delayed(() -> end(id), ticksLater);
        return get();
    }

    /**
     * Runs a task async to the
     * main MC thread
     *
     * @param runnable The code to run
     */
    public static int async(Runnable runnable)
    {
        return get().runTaskAsynchronously(Hyleria.get(), runnable).getTaskId();
    }

    /**
     * Runs a task after the
     * desired amount of time
     *
     * @param runnable The code to run
     * @param delay    The delay in ticks (20 ticks = 1 second)
     */
    public static int delayed(Runnable runnable, long delay)
    {
        return get().scheduleSyncDelayedTask(Hyleria.get(), runnable, delay);
    }

    /**
     * Runs a task synchronous to
     * the primary Minecraft thread
     *
     * @param runnable The code to run
     */
    public static int sync(Runnable runnable)
    {
        return get().runTask(Hyleria.get(), runnable).getTaskId();
    }

    /**
     * Runs a task at the rate provided.
     *
     * @param runnable the task
     * @param interval the time to run IN SECONDS
     * @return the ID of the task
     */
    public static int timer(Runnable runnable, long interval)
    {
        return timer(runnable, 0, interval);
    }

    /**
     * Runs a task at the rate provided
     *
     * @param runnable the task to run
     * @param delayBeforeRepeat how long to wait between running the task
     * @param interval how often to run this task IN SECONDS
     * @return the ID of the task
     */
    public static int timer(Runnable runnable, long delayBeforeRepeat, long interval)
    {
        return get().runTaskTimer(Hyleria.get(), runnable, delayBeforeRepeat, interval * 20).getTaskId();
    }

    /**
     * Runs a task at the rate provided
     *
     * @param runnable the task to run
     * @param interval how often to run this task in ticks
     * @return the ID of the task
     */
    public static int timerExact(Runnable runnable, long interval)
    {
        return get().runTaskTimer(Hyleria.get(), runnable, 0L, interval).getTaskId();
    }

}

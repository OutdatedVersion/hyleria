package com.hyleria.util;

import java.util.function.Consumer;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (5:51 PM)
 */
public class GameFlagHandler<T>
{

    /** the consumer backing this flag */
    private Consumer<T> consumer;

    /** whether or not we're processing this */
    private boolean enabled;

    /**
     * Start handling actions
     *
     * @return this handler
     */
    public GameFlagHandler<T> enable()
    {
        enabled = true;
        return this;
    }

    /**
     * Stop handling actions
     *
     * @return this handler
     */
    public GameFlagHandler<T> disable()
    {
        enabled = false;
        return this;
    }

    /**
     * @return the action backing this
     */
    public Consumer<T> action()
    {
        return consumer;
    }

    /**
     * Updates the action backing
     * this flag handler
     *
     * @param consumer the action
     * @return this handler
     */
    public GameFlagHandler<T> action(Consumer<T> consumer)
    {
         this.consumer = consumer;
         return this;
    }

    /**
     * Run this handler
     *
     * @param t what we're handling
     * @return this handler
     */
    public GameFlagHandler<T> accept(T t)
    {
        if (enabled)
            consumer.accept(t);

        return this;
    }

    /**
     * Returns a handler that is
     * enabled be default
     *
     * @param event the event we're covering
     * @param <T> type of said event
     * @return the fresh handler
     */
    public static <T> GameFlagHandler<T> enabled(Class<T> event)
    {
        return new GameFlagHandler<T>().enable();
    }

    /**
     * Returns a handler that is
     * disabled by default
     *
     * @param event the event we're covering
     * @param <T> type of that event
     * @return the fresh handler
     */
    public static <T> GameFlagHandler<T> disabled(Class<T> event)
    {
        return new GameFlagHandler<T>().disable();
    }

}

package com.hyleria.command.api;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/01/2017 (4:20 PM)
 */
public class Arguments implements Iterator<String>
{

    /** what the player typed */
    private final String[] raw;

    /** where we currently are in the raw arguments */
    private int currentPosition;

    public Arguments(String[] raw)
    {
        this.raw = raw;
    }

    /**
     * @return the next element in our arguments
     */
    @Override
    public String next()
    {
        return raw[currentPosition++];
    }

    /**
     * @return the next item in our arguments
     *         wrapped in an optional
     */
    public Optional<String> nextSafe()
    {
        return (raw.length > currentPosition + 1)
               ? Optional.empty()
               : Optional.of(raw[currentPosition++]);
    }

    /**
     * @return whether or not there's another
     *         element contained here
     */
    @Override
    public boolean hasNext()
    {
        return raw.length < currentPosition + 1;
    }

}

package com.hyleria.command.api;

import java.util.Optional;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/01/2017 (4:20 PM)
 */
public class Arguments
{

    private final String[] raw;

    private int currentPosition;

    public Arguments(String[] raw)
    {
        this.raw = raw;
    }

    /**
     * @return the next element in our args
     */
    public Optional<String> next()
    {
        return (raw.length > currentPosition + 1)
               ? Optional.empty()
               : Optional.of(raw[currentPosition++]);
    }

    /**
     * @return the next item in our arguments
     *         may throw an IOOB exception!
     */
    public String nextUnsafe()
    {
        return raw[currentPosition++];
    }

}

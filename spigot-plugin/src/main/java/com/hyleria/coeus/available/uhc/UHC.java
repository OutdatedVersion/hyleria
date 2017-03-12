package com.hyleria.coeus.available.uhc;

import com.hyleria.coeus.Game;

import java.time.Duration;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/08/2017 (5:42 PM)
 */
public class UHC implements Game
{

    /** the amount of time until you start healing */
    private Duration healTime = Duration.ofMinutes(10);

    /** the total time that must elapse before PvP is enabled */
    private Duration pvpTime = Duration.ofMinutes(20);

    /** */
    private Duration timeTillBorderShrink = Duration.ofMinutes(60 + 45);

    /** whether or not you may enter the nether */
    private boolean nether = true;

    /** */
    private boolean pearlDamage = true;

    /** */
    private boolean horseHealing = true;

    /**  */
    private int strength = 0;

    // handle stats engine side

    @Override
    public void init()
    {

    }

    @Override
    public void begin()
    {

    }

    @Override
    public void end()
    {

    }

}

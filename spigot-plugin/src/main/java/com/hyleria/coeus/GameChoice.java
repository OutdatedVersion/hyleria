package com.hyleria.coeus;

import com.hyleria.coeus.available.uhc.UHC;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (5:11 PM)
 */
public enum GameChoice
{

    UHC(UHC.class);

    /** the class containing all of the logic for this game */
    public final Class<? extends Game> clazz;

    /**
     * @param clazz the class of the game
     */
    GameChoice(Class<? extends Game> clazz)
    {
        this.clazz = clazz;
    }

}

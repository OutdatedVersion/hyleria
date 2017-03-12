package com.hyleria.coeus;

/**
 * Represents what's happening within
 * our game cycle at any given time.
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (4:52 PM)
 */
public enum Status
{

    INIT,          // the game itself is just being started - no mechanics
    MAP_FETCH,     // we're looking for maps that fit the current game type
    IDLE,          // we have the maps we need, but not the players. (voting/teams are handled here)
    COUNTDOWN,     // the required players have joined us; we're starting the countdown [may back-out still]
    NO_RETURN,     // we've hit 10 seconds left of the countdown; the map is being loaded now (unless it's handled specially)
    PRE_GAME,      // [actual match created] initial game logic is loading
    ACTIVE_GAME,   // the game has started
    END_GAME,      // the game has ended. preparing to cleanup match.
    CLEANUP        // let's start kicking users, and cleaning stuff up

}

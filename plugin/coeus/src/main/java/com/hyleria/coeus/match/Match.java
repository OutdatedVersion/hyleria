package com.hyleria.coeus.match;

import com.hyleria.coeus.GameChoice;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * Our game engine is based entirely on
 * theatrical "matches". Every time you
 * play a game, you're partaking
 * in a different match. The typical cycle
 * for a game will be as follows.. not done
 *
 * @author Ben (OutdatedVersion)
 * @since Mar/11/2017 (5:11 PM)
 */
public class Match
{

    // a unique ID for this match

    /** the game our players are playing this round */
    private GameChoice game;

    /**
     * The users who are currently PLAYING the
     * game, not just observing.
     */
    private List<Player> activePlayers;

    /**
     * Every user who participated in this match.
     * When the game starts we populate this with
     * everyone who is online.
     */
    private Set<MatchPlayer> players;

    // hold data pertaining to the map
    // full event log

}

package com.hyleria.coeus.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hyleria.coeus.Game;
import com.hyleria.common.math.Math;
import com.hyleria.common.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Set;

import static com.hyleria.util.Colors.bold;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.YELLOW;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/16/2017 (12:00 AM)
 */
public class PlayerScoreboard
{

    /** the player this board is tied to */
    private final Player player;

    /** the game used */
    private Game game;

    /** the backing scoreboard */
    private Scoreboard scoreboard;

    /** the backing objective */
    private Objective objective;

    /** the elements on this scoreboard */
    private List<String> elements;

    /** what's currently rendered on the scoreboard */
    private String[] current = new String[15];

    /** the title of the this scoreboard */
    private String title;

    /** point to cache the length of our title */
    private int titleLength;

    /** the last time we went through a full animation sequence */
    private long lastAnimationCycle = System.currentTimeMillis();

    /** where we are in the animation process */
    private int animationIndex = 0;

    public PlayerScoreboard(Player player)
    {
        this.player = player;

        this.elements = Lists.newArrayList();
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("h" + Math.random(999), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.setDisplayName(bold(GOLD) + "Hyleria");
    }

    /**
     * Updates the game that backs
     * this scoreboard
     *
     * @param game the game
     * @return the player's scoreboard
     */
    public PlayerScoreboard game(Game game)
    {
        this.game = game;
        return this;
    }

    /**
     * Updates the title of the scoreboard
     * objective
     *
     * @param title the new title
     * @return this scoreboard
     */
    public PlayerScoreboard title(String title)
    {
        this.title = title;
        this.titleLength = title.length();

        return this;
    }

    /**
     * @return the player this scoreboard is for
     */
    public Player player()
    {
        return player;
    }

    /** actually send out the scoreboard */
    public void draw()
    {
        final List<String> _fresh = Lists.newArrayList();

        for (String line : elements)
        {
            while (true)
            {
                boolean matched = false;

                for (String otherLine : _fresh)
                {
                    if (line.equals(otherLine))
                    {
                        line += ChatColor.RESET;
                        matched = true;
                    }
                }

                if (!matched)
                    break;
            }

            _fresh.add(line);
        }

        final Set<Integer> _adding = Sets.newHashSet();
        final Set<Integer> _deleting = Sets.newHashSet();

        for (int i = 0; i < 15; i++)
        {
            if (i >= _fresh.size())
            {
                if (current[i] != null)
                    _deleting.add(i);

                continue;
            }

            if (current[i] == null || !current[i].equals(_fresh.get(i)))
            {
                _deleting.add(i);
                _adding.add(i);
            }
        }

        _deleting.stream().filter(i -> current[i] != null).forEach(i ->
        {
            scoreboard.resetScores(current[i]);
            current[i] = null;
        });

        for (int i : _adding)
        {
            String newLine = _fresh.get(i);
            objective.getScore(newLine).setScore(15 - i);
            current[i] = newLine;
        }

        int _size = nonNullSize();
        for (int i = 0; i < _size; i++)
        {
            objective.getScore(_fresh.get(i)).setScore(_size - i);
        }
    }

    /**
     * Clear the contents of this scoreboard
     */
    public void purge()
    {
        elements.clear();
    }

    /**
     * Adds a blank line to the scoreboard
     */
    public void blank()
    {
        elements.add(" ");
    }

    /**
     * Write the top part of some two function line
     *
     * @param title the text
     */
    public void writeHead(String title)
    {
        write(ChatColor.BOLD + "» " + title);
    }

    /**
     * Write the provided text to the scoreboard
     *
     * @param content the text
     */
    public void write(Object content)
    {
        // 1.7 only allows 16 total characters on the scoreboard - including color codes!
        final String _asString = String.valueOf(content);
        elements.add(_asString.substring(0, _asString.length() < 16 ? _asString.length() : 16));
    }

    /**
     * A shortcut to writing our address
     */
    public void writeURL()
    {
        blank();
        elements.add(ChatColor.YELLOW + "hyleria.com");
    }

    /**
     * Cycle the animation
     */
    public void animationTick()
    {
        if (!TimeUtil.elapsed(lastAnimationCycle, 3200))
            return;


        objective.setDisplayName(addAnimation(title));

        if (animationIndex++ == (titleLength + 4))
        {
            animationIndex = 0;
            lastAnimationCycle = System.currentTimeMillis();
        }
    }

    /**
     * Process the animation cycle
     *
     * @param to what we're writing
     * @return the reformatted text
     */
    private String addAnimation(String to)
    {
        String _working = bold(GOLD);

        if (animationIndex == to.length() + 1 || animationIndex == to.length() + 3)
            _working += bold(YELLOW) + to;
        else if (animationIndex == to.length() + 2 || animationIndex == to.length() + 4)
            _working += bold(GOLD) + to;
        else
        {
            for (int i = 0; i < to.length(); i++)
            {
                char _character = to.charAt(i);

                if (i == animationIndex)
                    _working += bold(YELLOW) + _character + bold(GOLD);
                else
                    _working += _character;
            }
        }

        return _working;
    }

    /**
     * @return the size of the elements in this
     *         scoreboard that are NOT null
     */
    private int nonNullSize()
    {
        int _counter = 0;

        for (String line : current)
            if (line != null)
                _counter++;

        return _counter;
    }

    /**
     * @return the actual scoreboard backing this 'wrapper'
     */
    public Scoreboard bukkitScoreboard()
    {
        return scoreboard;
    }

}

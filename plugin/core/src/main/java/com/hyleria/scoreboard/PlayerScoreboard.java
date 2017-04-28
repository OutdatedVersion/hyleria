package com.hyleria.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import static org.bukkit.ChatColor.*;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/16/2017 (12:00 AM)
 */
public class PlayerScoreboard
{

    /** the player this board is tied to */
    private final Player player;

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

        objective.setDisplayName(bold(BLUE) + "Hyleria");
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
            final StringBuilder _line = new StringBuilder(line);

            while (true)
            {
                boolean matched = false;

                for (String otherLine : _fresh)
                {
                    if (line.equals(otherLine))
                    {
                        _line.append(ChatColor.RESET);
                        matched = true;
                    }
                }

                if (!matched)
                    break;
            }

            _fresh.add(_line.toString());
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
    public PlayerScoreboard blank()
    {
        elements.add(" ");
        return this;
    }

    /**
     * Write the top part of some two function line
     *
     * @param title the text
     */
    public void writeHead(String title)
    {
        write(GRAY + "» " + GREEN + title);
    }

    /**
     * Write the provided text in a
     * single line key-value format.
     *
     * @param prefix the "key"
     * @param content the "value"
     */
    public PlayerScoreboard write(String prefix, Object content)
    {
        return write(GREEN + prefix + ": " + WHITE + String.valueOf(content));
    }

    /**
     * Write the provided text to the scoreboard
     *
     * @param content the text
     */
    public PlayerScoreboard write(Object content)
    {
        // 1.7 only allows 16 total characters on the scoreboard - including color codes!
        final String _asString = String.valueOf(content);
        elements.add(_asString.substring(0, _asString.length() < 16 ? _asString.length() : 16));

        return this;
    }

    /**
     * A shortcut to writing our address
     */
    public PlayerScoreboard writeURL()
    {
        return blank().write(AQUA + "hyleria.com");
    }

    /**
     * Cycle the animation
     */
    void animationTick()
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
        final StringBuilder _working = new StringBuilder(bold(BLUE));

        if (animationIndex == to.length() + 1 || animationIndex == to.length() + 3)
            _working.append(bold(AQUA)).append(to);
        else if (animationIndex == to.length() + 2 || animationIndex == to.length() + 4)
            _working.append(bold(BLUE)).append(to);
        else
        {
            for (int i = 0; i < to.length(); i++)
            {
                char _character = to.charAt(i);

                if (i == animationIndex)
                    _working.append(bold(AQUA)).append(_character).append(bold(BLUE));
                else
                    _working.append(_character);
            }
        }

        return _working.toString();
    }

    /**
     * @return the actual scoreboard backing this 'wrapper'
     */
    public Scoreboard bukkitScoreboard()
    {
        return scoreboard;
    }

}

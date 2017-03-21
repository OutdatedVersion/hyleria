package com.hyleria.coeus.available.uhcgames.utils;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static com.hyleria.common.math.Math.chance;

/**
 * @author Jp78
 * @since Sunday Mar/17
 */
public enum Tier
{

    BAD(90, 50, 0),
    LOW(95, 75, 7),
    HIGH(100, 75, 25);

    private Integer badChance;
    private Integer averageChance;
    private Integer goodChance;

    /**
     * Create a tier with the chances of getting an average item in a chest vs a good one. <strong>All chances have to
     * be between 1 and 100!</strong>
     *
     * @param badChance     The chance of getting a bad item
     * @param averageChance The chance of getting an average item, so that some items won't appear or empty chests
     * @param goodChance    The chance of getting a good item
     */
    Tier(Integer badChance, Integer averageChance, Integer goodChance)
    {
        this.badChance = badChance;
        this.averageChance = averageChance;
        this.goodChance = goodChance;
    }

    public Integer averageChance()
    {
        return averageChance;
    }

    public Integer badChance()
    {
        return badChance;
    }

    public Integer goodChance()
    {
        return goodChance;
    }

    /**
     * Converts the given max into a chance based off our badChance, averageChance and good chance. This determines
     * how many items of the type the chest will receive, if any.
     *
     * @param max        The max amount of items one can receive
     * @param itemChance The item chance type, i.e if its a bad, average or good item.
     *
     * @return How many items the chest will receive, if any.
     */
    public Integer chanceItem(Integer max, Chance itemChance)
    {
        switch ( itemChance )
        {
            case BAD: return maxChance(max,badChance);
            case AVERAGE: return maxChance(max,averageChance);
            case GOOD: return maxChance(max,goodChance);
        }

        throw new RuntimeException("Chance instance did not match available chances! THIS IS NOT POSSIBLE");
    }

    private Integer maxChance(Integer max, Integer chance)
    {
         return IntStream.range(0, max).filter(i -> chance(chance)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll).size();
    }

    public enum Chance
    {
        BAD, AVERAGE, GOOD
    }

}

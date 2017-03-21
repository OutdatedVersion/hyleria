package com.hyleria.coeus.available.uhcg.utils;

import com.google.common.collect.Lists;
import com.hyleria.common.math.Math;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * This file was created by @author thejp for the use of
 * hyleria. Please note, all rights to code are retained by
 * afore mentioned thejp unless otherwise stated.
 * File created: Sunday, March, 2017
 */
public class ChestPopulator
{


    /** Our list of bad items that'll be put in chests */
    private final List<ChestItem> badItems = null;
    /**
     * Our list of "normal" items that'll be put in chests
     */
    private final List<ChestItem> averageItems = null;
    /**
     * Our list of "good" items
     */
    private final List<ChestItem> goodItems = null;

    private final Integer maxBad = 8;
    private final Integer maxAverage = 4;
    private final Integer maxGood = 2;

    /** Our blocks that have already been populated, so they won't be repopulated by our algorithm */
    public List<Location> populated = Lists.newArrayList();


    /**
     * Populates a chest based on its tier and in accordance to our lists.
     *
     * @param c    The chest to populate
     * @param tier The tier of said chest
     */
    public void populate(Chest c, Tier tier)
    {
        if (populated.contains(c.getLocation()))
            return;
        populated.add(c.getLocation());
        //How many bad items we will put
        Integer includedBad = tier.chanceItem(maxBad, Tier.Chance.BAD);
        //How many average items
        Integer includedAverage = tier.chanceItem(maxAverage, Tier.Chance.AVERAGE);
        //How many good items
        Integer includedGood = tier.chanceItem(maxGood, Tier.Chance.AVERAGE);
        //How many total items will be put in this chest.
        Integer totalItems = includedBad + includedAverage + includedGood;

        //Where we will put these items
        Set<Integer> slots = IntStream.range(0, totalItems).map(i -> Math.random(0, 26)).collect(HashSet::new, HashSet::add, HashSet::addAll);
        while ( slots.size() != totalItems )
        {
            slots = IntStream.range(0, totalItems).map(i -> Math.random(0, 26)).collect(HashSet::new, HashSet::add, HashSet::addAll); //To ensure we have 6 exact
        }
        Iterator<Integer> slotsit =  slots.iterator();
        IntStream.range(0, includedBad).forEach(i ->
        {
            c.getBlockInventory().setItem(slotsit.next(),badItems.get(Math.random(0,badItems.size() + 1)).toItemstack());
        });
        IntStream.range(0, includedAverage).forEach(i ->
        {
            c.getBlockInventory().setItem(slotsit.next(),averageItems.get(Math.random(0,averageItems.size() + 1)).toItemstack());
        });
        IntStream.range(0, includedGood).forEach(i ->
        {
            c.getBlockInventory().setItem(slotsit.next(),goodItems.get(Math.random(0,goodItems.size() + 1)).toItemstack());
        });
    }


    /**
     * Utility method to convert a given material and integer to a chest item.
     *
     * @param m   The material
     * @param max Max items
     *
     * @return A new chest item
     */
    private ChestItem convert(Material m, Integer max)
    {
        return new ChestItem(m, max);
    }


    private static class ChestItem
    {

        /**
         * The material of this chest item.
         */
        private Material chestMaterial;

        /**
         * The maximum amount of this item in a chest.
         */
        private Integer maximumItems;

        public ChestItem(Material chestMaterial, Integer maximumItems)
        {
            this.chestMaterial = chestMaterial;
            this.maximumItems = maximumItems;
        }

        /**
         * Convert this ChestItem to a random item stack with a random number between 1 and the maximum amount of
         * items and the material.
         *
         * @return The item stack
         */
        public ItemStack toItemstack()
        {
            return new ItemStack(chestMaterial, maximumItems);
        }
    }
}

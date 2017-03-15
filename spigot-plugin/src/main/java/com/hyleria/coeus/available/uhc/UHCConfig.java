package com.hyleria.coeus.available.uhc;

import com.google.gson.annotations.SerializedName;
import com.hyleria.common.backend.GameConfiguration;

import java.util.Set;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/14/2017 (7:54 PM)
 */
public class UHCConfig implements GameConfiguration
{

    /** the <strong>CODE</strong> names of the scenarios we'll be using. (their class name) */
    @SerializedName ( "enabled_scenarios" )
    public Set<String> enabledScenarios;

    /** the amount of time until you start healing (in MINUTES) */
    @SerializedName ( "heal_time" )
    public int healTime = 10;

    /** the total time that must elapse before PvP is enabled */
    @SerializedName ( "pvp_time" )
    public int pvpTime = 20;

    /** the radius of the border (x side) */
    @SerializedName ( "radius_x" )
    public int radiusX;

    /** the radius of the border (z side) */
    @SerializedName ( "radius_z" )
    public int radiusZ;

    /** the time that must elapse before the border starts to shrink */
    @SerializedName ( "time_till_border_shrink" )
    public int timeTillBorderShrink = 60 + 45;

    /** whether or not you may enter the nether */
    @SerializedName ( "nether_enabled" )
    public boolean nether = true;

    /** whether or not damage should be dealt when you hit the ground */
    @SerializedName ( "pearl_damage" )
    public boolean pearlDamage = true;

    /** whether or not your horse should be healed */
    @SerializedName ( "horse_healing" )
    public boolean horseHealing = true;

    /** whether or not strength ONE pots can be brewed */
    @SerializedName ( "strength_one" )
    public boolean allowStrengthOne;

    /** whether or not strength TWO pots can be brewed */
    @SerializedName ( "strength_two" )
    public boolean allowStrengthTwo;

}

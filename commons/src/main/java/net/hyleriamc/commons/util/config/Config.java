package net.hyleriamc.commons.util.config;

/**
 * Allows the injection of {@link Configurations}
 * via our {@link com.google.inject.Guice} module
 *
 * @author Ben (OutdatedVersion)
 * @since Jan/22/2017 (2:38 PM)
 */
public @interface Config
{

    /**
     * @return the class for this configuration
     */
    Class<?> value() default Config.class;

    /**
     * @return the path to the configuration file
     */
    String path() default "";

    /**
     * @return the configuration class we're looking for
     */
    Class<?> clazz() default Config.class;

}

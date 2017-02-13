package net.hyleriamc.commons.util.config;

/**
 * Represents a configuration file for
 * anything that may require one. These
 * are intended to be easily injected via
 * {@link com.google.inject.Guice}.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/05/2017 (2:46 PM)
 *
 * @see Config
 * @see Configurations
 */
public interface Configuration
{

    /**
     *
     * @return
     */
    String defaultPath();

}

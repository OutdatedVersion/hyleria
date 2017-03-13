package com.hyleria.common.backend;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/13/2017 (4:55 PM)
 */
public enum ConfigurationMode
{

    /**
     * Data is loaded from one
     * centralized source. Probably
     * the file system, or some web
     * endpoint.
     */
    SHARED,

    /**
     * Every server will hold a
     * file configured for that
     * specific server. Most extended
     * games will follow this practice.
     * Whilst "mini-game" like servers
     * will follow the shared implementation.
     */
    DEDICATED

}

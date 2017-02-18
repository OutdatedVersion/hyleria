package net.hyleriamc.commons.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the specified class
 * is a module started on all servers.
 * As such, it should be started when
 * a server is firing up.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/08/2017 (6:04 PM)
 */
@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.TYPE )
public @interface StartParallel
{

}

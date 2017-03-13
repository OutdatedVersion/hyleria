package com.hyleria.common.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Let's our loader know that the
 * specified class requires these
 * other modules before itself.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/18/2017 (4:00 PM)
 */
@Target ( ElementType.TYPE )
@Retention ( RetentionPolicy.RUNTIME )
public @interface Requires
{

    /**
     * @return the classes
     */
    Class<?>[] value();

}

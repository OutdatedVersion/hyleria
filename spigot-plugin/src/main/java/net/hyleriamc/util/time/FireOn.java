package net.hyleriamc.util.time;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OutdatedVersion
 * Dec/14/2016 (6:43 PM)
 */

@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.METHOD )
public @interface FireOn
{

    /**
     * @return the value (amount) of our unit
     */
    int value() default 1;

    /**
     * @return the length of time we're using
     */
     TimeLength length() default TimeLength.SECOND;

}

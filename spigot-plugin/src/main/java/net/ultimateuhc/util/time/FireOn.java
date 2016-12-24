package net.ultimateuhc.util.time;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * OutdatedVersion
 * Dec/14/2016 (6:43 PM)
 */

@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.METHOD )
public @interface FireOn
{

    /**
     * @return the value (amount) of our units
     */
    int value() default 1;

    /**
     * @return the length of time we're using
     */
    TimeUnit unit() default TimeUnit.SECONDS;

}

package com.hyleria.commons.translation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the specified field
 * will be converted to the provided
 * {@link Class} before saving it
 * to the account.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/22/2017 (10:49 PM)
 */
@Target ( ElementType.FIELD )
@Retention ( RetentionPolicy.RUNTIME )
public @interface SaveAs
{

    /**
     * @return the class to be converted
     *         to before saving
     */
    Class<?> value();

}

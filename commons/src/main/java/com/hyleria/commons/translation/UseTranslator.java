package com.hyleria.commons.translation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated
 * field should be ran through
 * the specified translator before
 * being saved.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/24/2017 (11:52 AM)
 */
@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.FIELD )
public @interface UseTranslator
{

    /**
     * @return the translator to use
     */
    Translators value();

}

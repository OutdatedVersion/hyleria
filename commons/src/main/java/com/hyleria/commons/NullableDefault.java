package com.hyleria.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/22/2017 (11:16 PM)
 */
@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.FIELD )
public @interface NullableDefault
{

}

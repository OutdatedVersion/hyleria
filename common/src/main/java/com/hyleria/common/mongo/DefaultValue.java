package com.hyleria.common.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ben (OutdatedVersion)
 * @since Feb/22/2017 (11:20 PM)
 */
@Target ( ElementType.FIELD )
@Retention ( RetentionPolicy.RUNTIME )
public @interface DefaultValue
{

}

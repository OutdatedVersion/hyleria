package com.hyleria.common.redis.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (3:07 PM)
 */
@Target ( ElementType.METHOD )
@Retention ( RetentionPolicy.RUNTIME )
public @interface HandlesType
{

    Class<? extends Payload> value();

}

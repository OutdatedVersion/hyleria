package com.hyleria.command.api.annotation;

import com.hyleria.common.reference.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/01/2017 (4:08 PM)
 */
@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.METHOD )
public @interface Permission
{

    /**
     * @return the role that someone needs
     *         to run the annotated command
     */
    Role value();

    /**
     * @return the message we send in case
     *         the player doesn't have the
     *         role
     */
    String note() default "DEFAULT_MESSAGE";

}

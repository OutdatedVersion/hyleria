package com.hyleria.commons.inject;

import com.google.inject.BindingAnnotation;
import com.google.inject.Stage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows the injection of {@link ConfigurationProvider}
 * via our {@link com.google.inject.Guice} module
 *
 * @author Ben (OutdatedVersion)
 * @since Jan/22/2017 (2:38 PM)
 */
@Target ( { ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD } )
@Retention ( RetentionPolicy.RUNTIME )
@BindingAnnotation
public @interface Config
{

    /**
     * Holds the path to the config file that this
     * annotation is letting guice know to inject.
     * Certain variables may be used in this {@link String}.
     *
     * <ul>
     *     <li>{@code env}, replaced with the current environment we're in. Represented by: {@link Stage}</li>
     * </ul>
     *
     * @return the path to this configuration file
     */
    String value();

}

package net.hyleriamc.commons.inject;

import com.google.inject.BindingAnnotation;

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



}

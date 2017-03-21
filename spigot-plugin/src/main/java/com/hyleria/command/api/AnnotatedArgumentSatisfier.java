package com.hyleria.command.api;

import java.lang.annotation.Annotation;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/21/2017 (11:31 AM)
 */
public interface AnnotatedArgumentSatisfier<T> extends ArgumentSatisfier<T>
{

    Class<? extends Annotation> requiresAnnotation();

}

package com.hyleria.commons.translation;

/**
 * Indicates that the specified field
 * will be converted to the provided
 * {@link Class} before saving it
 * to the database.
 *
 * @author Ben (OutdatedVersion)
 * @since Feb/22/2017 (10:49 PM)
 */
public @interface SaveAs
{

    /**
     * @return the class to be converted
     *         to before saving
     */
    Class<?> value();

}

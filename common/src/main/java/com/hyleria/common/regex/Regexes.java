package com.hyleria.common.regex;

import java.util.regex.Pattern;

/**
 * @author Ben (OutdatedVersion)
 * @since Apr/19/2017 (10:21 PM)
 */
public class Regexes
{

    /** allows us to check that a {@link String} is in fact an {@link java.util.UUID} */
    public static final Pattern UUID = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

}

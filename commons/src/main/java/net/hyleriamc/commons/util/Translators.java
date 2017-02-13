package net.hyleriamc.commons.util;

import java.util.UUID;
import java.util.function.Function;

 /**
  * @author Ben (OutdatedVersion)
  * @since Jan/02/2017 (9:53 PM)
  */
public class Translators
{

    // removed that whole object-object translation system
    // that previously existed. was pretty messy.

    /** convert a Java {@link String} into a {@link UUID} */
    public static final Function<String, UUID> STRING_TO_UUID = string -> string.contains("-")
                                                                          ? UUID.fromString(string)
                                                                          : new UUID(Long.parseUnsignedLong(string.substring(0, 16), 16),
                                                                                     Long.parseUnsignedLong(string.substring(16), 16));

}

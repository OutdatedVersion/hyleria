package net.ultimateuhc.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

/**
 * OutdatedVersion
 * Dec/23/2016 (10:04 PM)
 */

public class Translators
{

    /** default translators */
    private static List<Translator<?, ?>> HOLD = Lists.newArrayList();

    // populate..
    static
    {
        HOLD.add(new Translator<java.util.UUID, String>()
        {
            @Override
            public UUID read(String raw)
            {
                return java.util.UUID.fromString(raw);
            }

            @Override
            public String write(UUID raw)
            {
                return raw.toString();
            }
        });
    }

    /**
     * Returns a {@link Translator} for the provided type.
     *
     * @param clazzOne the code safe version
     * @param clazzTwo the database safe version
     * @param <A> type of class one
     * @param <B> type of class two
     * @return a translator (an NPE will be thrown if one does not exist for the provided classes)
     */
    public static <A, B> Translator<A, B> translatorFor(Class<A> clazzOne, Class<B> clazzTwo)
    {
        // I have no clue man.. this just seems like a terrible solution.

        return (Translator<A, B>) HOLD
                                  .stream()
                                  .filter(translator -> translator.codeSafe.getClass() == clazzOne && translator.databaseSafe.getClass() == clazzTwo)
                                  .findFirst().get();
    }

}

package com.hyleria.commons.translation;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Set;
import java.util.UUID;

/**
 * @author Ben (OutdatedVersion)
 * @since Jan/02/2017 (9:53 PM)
 */
public class Translators
{

    /** a set of our default translators */
    private static Set<Translator> translatorHold = Sets.newHashSet();

    // insert our default translators
    static
    {
        translatorHold.add(new Translator<UUID, String>()
        {
            @Override
            UUID fromFirst(String string)
            {
                return string.contains("-")
                       ? UUID.fromString(string)
                       : new UUID(Long.parseUnsignedLong(string.substring(0, 16), 16),
                                  Long.parseUnsignedLong(string.substring(16), 16));
            }

            @Override
            String fromSecond(UUID uuid)
            {
                return uuid.toString();
            }
        });
    }

    /**
     * Attempts to grab a {@link Translator}
     * that works for the provided types.
     *
     * @param from one of the classes
     * @param to the other class
     * @param <A> the type of the first class
     * @param <B> the type of the second class
     * @return either the translator, or {@code null}.
     *         there's a pretty strong chance you know
     *         one already exists for what you're working
     *         with when using this method though.
     */
    public static <A, B> Translator<A, B> translatorFor(Class<A> from, Class<B> to)
    {
        return translatorHold
                .stream()
                .filter(translator -> new EqualsBuilder().append(translator.a.getClass() == from,
                                        translator.b.getClass() == to).build()).findFirst().get();
    }

}

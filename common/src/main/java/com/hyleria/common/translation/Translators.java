package com.hyleria.common.translation;

import com.hyleria.common.account.Account;
import org.bson.Document;

import java.util.UUID;

/**
 * Holds a bunch of random {@link Translator}s
 * that we may need to use.
 *
 * @author Ben (OutdatedVersion)
 * @since Jan/02/2017 (9:53 PM)
 */
public enum Translators
{

    // I have no idea what I was thinking with this class
    // like tf

    UUID(new Translator<UUID, String>()
    {
        @Override
        public UUID read(String string)
        {
            return string.contains("-")
                   ? java.util.UUID.fromString(string)
                   : new UUID(Long.parseUnsignedLong(string.substring(0, 16), 16),
                              Long.parseUnsignedLong(string.substring(16), 16));
        }

        @Override
        public String write(UUID uuid)
        {
            return uuid.toString();
        }
    }),

    PREVIOUS_ADDRESS(new Translator<Account.PreviousAddress, Document>()
    {
        @Override
        public Account.PreviousAddress read(Document document)
        {
            return new Account.PreviousAddress(document.getString("val"), document.getLong("last_used"));
        }

        @Override
        public Document write(Account.PreviousAddress previousAddress)
        {
            return new Document("last_used", previousAddress.lastUsedOn).append("val", previousAddress.value);
        }
    });

    /** the translator behind this */
    private final Translator translator;

    /**
     * @param translator the translator behind this
     */
    Translators(Translator translator)
    {
        this.translator = translator;
    }

    /**
     * @return the backing translator
     */
    public Translator get()
    {
        return translator;
    }

}

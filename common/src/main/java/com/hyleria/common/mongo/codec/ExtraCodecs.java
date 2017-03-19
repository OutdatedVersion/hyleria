package com.hyleria.common.mongo.codec;

import com.google.common.collect.Lists;
import com.hyleria.common.account.Account;
import com.hyleria.common.reference.Role;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/18/2017 (11:28 AM)
 */
public class ExtraCodecs
{

    /** all of the custom codecs we use */
    public static final List<? extends Codec<?>> HYLERIA_CODECS = Lists.newArrayList(new RoleCodec(), new PreviousAddressCodec(), new UUIDCodec());

    /** the fresh UUID */
    public static Function<String, UUID> UNDASHED_UUID_PARSER = val -> new UUID(Long.parseUnsignedLong(val.substring(0, 16), 16),
                                                                                Long.parseUnsignedLong(val.substring(16), 16));

    /**
     * Turn our permission roles into enums from strings (& vice versa)
     */
    static class RoleCodec implements Codec<Role>
    {
        @Override
        public Role decode(BsonReader reader, DecoderContext decoderContext)
        {
            return Role.valueOf(reader.readString());
        }

        @Override
        public void encode(BsonWriter writer, Role value, EncoderContext encoderContext)
        {
            writer.writeString(value.name());
        }

        @Override
        public Class<Role> getEncoderClass()
        {
            return Role.class;
        }
    }

    /**
     * UUID <-> String
     *
     * BSON has one of these included?
     */
    static class UUIDCodec implements Codec<UUID>
    {
        @Override
        public UUID decode(BsonReader reader, DecoderContext context)
        {
            final String _val = reader.readString();

            return _val.contains("-")
                   ? java.util.UUID.fromString(_val)
                   : UNDASHED_UUID_PARSER.apply(_val);
        }

        @Override
        public void encode(BsonWriter writer, UUID value, EncoderContext context)
        {
            writer.writeString(value.toString());
        }

        @Override
        public Class<UUID> getEncoderClass()
        {
            return UUID.class;
        }
    }

    /**
     * {@link com.hyleria.common.account.Account.PreviousAddress} <-> {@link org.bson.Document}
     */
    static class PreviousAddressCodec implements Codec<Account.PreviousAddress>
    {
        @Override
        public Account.PreviousAddress decode(BsonReader reader, DecoderContext decoderContext)
        {
            reader.readStartDocument();
            final String _val = reader.readString("val");
            final long _lastUsed = reader.readInt64("last_used");
            reader.readEndDocument();

            return new Account.PreviousAddress(_val, _lastUsed);
        }

        @Override
        public void encode(BsonWriter writer, Account.PreviousAddress value, EncoderContext encoderContext)
        {
            writer.writeStartDocument();
            writer.writeString("val", value.value);
            writer.writeInt64("last_used", value.lastUsedOn);
            writer.writeEndDocument();
        }

        @Override
        public Class<Account.PreviousAddress> getEncoderClass()
        {
            return Account.PreviousAddress.class;
        }
    }

}

package com.hyleria.common.backend.payload;

import com.google.gson.annotations.SerializedName;
import com.hyleria.common.json.JSONBuilder;
import com.hyleria.common.redis.RedisChannel;
import com.hyleria.common.redis.api.Focus;
import com.hyleria.common.redis.api.Payload;
import com.hyleria.common.reference.Role;
import org.json.simple.JSONObject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Ben (OutdatedVersion)
 * @since Mar/25/2017 (6:47 PM)
 */
@Focus ( "gen-staff-chat" )
public class StaffChatPayload implements Payload
{

    /** "[Role] Name" */
    public String name;

    /** "[Role] Name" */
    public Role role;

    /** color for {@link #role} */
    @SerializedName ( "color_enum" )
    public String colorEnum;

    /** message content for chat */
    public String message;

    /** the server this was sent on */
    @SerializedName ( "server_sent_on" )
    public String sentOn;

    public StaffChatPayload(String name, Role role, String colorEnum, String message, String sentOn)
    {
        this.name = checkNotNull(name);
        this.role = checkNotNull(role);
        this.colorEnum = checkNotNull(colorEnum);
        this.message = checkNotNull(message);
        this.sentOn = checkNotNull(sentOn);
    }

    @Override
    public JSONObject asJSON()
    {
        return JSONBuilder.builder().addAllFields(this).asJSON();
    }

    @Override
    public RedisChannel channel()
    {
        return RedisChannel.DEFAULT;
    }

}

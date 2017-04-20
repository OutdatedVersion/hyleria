package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

 /**
  * When a player falls into the
  * void (past Y: 0) we'll encounter
  * this event.
  *
  * @author Ben (OutdatedVersion)
  * @since 9:31 PM (Jul/31/2016)
  */
public class PlayerVoidDamageEvent extends DamageEvent
{

    public PlayerVoidDamageEvent(Player victim, double dealtDamage)
    {
        super(victim, dealtDamage, DamageCause.ENVIRONMENT);
    }

    @Override
    public BaseComponent[] information()
    {
        return new ComponentBuilder("").append("World Void - ").color(ChatColor.GRAY)
                .append(String.valueOf(damageDealt())).color(ChatColor.RED).create();
    }

}

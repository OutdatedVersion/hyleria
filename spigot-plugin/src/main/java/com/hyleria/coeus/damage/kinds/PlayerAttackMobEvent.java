package com.hyleria.coeus.damage.kinds;

import com.hyleria.coeus.damage.DamageCause;
import com.hyleria.coeus.damage.DamageEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Whenever some player attacks a mob
 * it will be ran through this event.
 *
 * @author Ben (OutdatedVersion)
 * @since 5:38 PM (May/17/2016)
 */
public class PlayerAttackMobEvent extends DamageEvent
{

    /** what the player attacked */
    private Entity attacked;

    public PlayerAttackMobEvent(Player attacker, Entity attacked, double dealtDamage)
    {
        super(attacker, dealtDamage, DamageCause.MOB);

        this.attacked = attacked;
    }

    /**
     * @return the attacked entity
     */
    public Entity target()
    {
        return attacked;
    }

    @Override
    public BaseComponent[] information()
    {
        return new BaseComponent[0];
    }

}

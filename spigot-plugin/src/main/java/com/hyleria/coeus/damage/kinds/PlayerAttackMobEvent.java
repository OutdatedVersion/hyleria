package com.simplexitymc.kraken.damage.kinds;

import com.simplexitymc.kraken.damage.DamageCause;
import com.simplexitymc.kraken.damage.DamageEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * OutdatedVersion
 * At: 5:38 PM (May/17/2016)
 * cave-realms
 */

public class PlayerAttackMobEvent extends DamageEvent
{

    private Entity attacked;

    public PlayerAttackMobEvent(Player attacker, Entity attacked, double dealtDamage)
    {
        super(attacker, dealtDamage, DamageCause.MOB);

        this.attacked = attacked;
    }

    public Entity getTarget()
    {
        return attacked;
    }

    @Override
    public BaseComponent[] getInformation()
    {
        return new BaseComponent[0];
    }

}

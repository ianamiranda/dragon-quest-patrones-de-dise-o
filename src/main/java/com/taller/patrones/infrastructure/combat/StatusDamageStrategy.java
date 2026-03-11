package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class StatusDamageStrategy implements DamageStrategy {

    @Override
    public int calculate(Character attacker, Character defender, Attack attack) {
        return 0;
    }
}

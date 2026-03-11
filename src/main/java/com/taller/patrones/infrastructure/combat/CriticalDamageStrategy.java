package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

import java.util.concurrent.ThreadLocalRandom;

public class CriticalDamageStrategy implements DamageStrategy {

    private static final int CRITICAL_CHANCE_PERCENT = 20;

    @Override
    public int calculate(Character attacker, Character defender, Attack attack) {
        int raw = attacker.getAttack() * attack.getBasePower() / 100;
        int baseDamage = Math.max(1, raw - defender.getDefense());
        boolean isCritical = ThreadLocalRandom.current().nextInt(100) < CRITICAL_CHANCE_PERCENT;
        return isCritical ? (baseDamage * 3) / 2 : baseDamage;
    }
}

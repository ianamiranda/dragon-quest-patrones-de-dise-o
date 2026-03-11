package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Estrategia para calcular daño según tipo de ataque.
 */
public interface DamageStrategy {

    int calculate(Character attacker, Character defender, Attack attack);
}

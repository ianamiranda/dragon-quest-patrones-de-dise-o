package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

import java.util.EnumMap;
import java.util.Map;

/**
 * Motor de combate. Calcula daño y crea ataques.
 * <p>
 * Nota: Esta clase crece cada vez que añadimos un ataque nuevo o un tipo de daño distinto.
 */
public class CombatEngine {

    private final AttackFactory attackFactory;
    private final Map<Attack.AttackType, DamageStrategy> damageStrategies;

    public CombatEngine() {
        this.attackFactory = new DefaultAttackFactory();
        this.damageStrategies = new EnumMap<>(Attack.AttackType.class);
        registerDefaultDamageStrategies();
    }

    /**
     * Crea un ataque a partir de su nombre.
     * Cada ataque nuevo requiere modificar este método.
     */
    public Attack createAttack(String name) {
        return attackFactory.create(name);
    }

    /**
     * Calcula el daño según el tipo de ataque.
     * Cada fórmula está encapsulada en una estrategia intercambiable.
     */
    public int calculateDamage(Character attacker, Character defender, Attack attack) {
        if (attacker == null || defender == null || attack == null) {
            return 0;
        }

        DamageStrategy strategy = damageStrategies.get(attack.getType());
        if (strategy == null) {
            return 0;
        }
        return strategy.calculate(attacker, defender, attack);
    }

    public void registerDamageStrategy(Attack.AttackType type, DamageStrategy strategy) {
        damageStrategies.put(type, strategy);
    }

    private void registerDefaultDamageStrategies() {
        registerDamageStrategy(Attack.AttackType.NORMAL, new NormalDamageStrategy());
        registerDamageStrategy(Attack.AttackType.SPECIAL, new SpecialDamageStrategy());
        registerDamageStrategy(Attack.AttackType.STATUS, new StatusDamageStrategy());
        registerDamageStrategy(Attack.AttackType.CRITICAL, new CriticalDamageStrategy());
    }
}

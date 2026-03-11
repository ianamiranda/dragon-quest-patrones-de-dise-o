package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factoria basada en registro de ataques.
 * Permite agregar nuevos ataques sin modificar CombatEngine.
 */
public class DefaultAttackFactory implements AttackFactory {

    private static final Attack DEFAULT_ATTACK = new Attack("Golpe", 30, Attack.AttackType.NORMAL);
    private final Map<String, Supplier<Attack>> attackRegistry = new HashMap<>();

    public DefaultAttackFactory() {
        register("TACKLE", () -> new Attack("Tackle", 40, Attack.AttackType.NORMAL));
        register("SLASH", () -> new Attack("Slash", 55, Attack.AttackType.NORMAL));
        register("FIREBALL", () -> new Attack("Fireball", 80, Attack.AttackType.SPECIAL));
        register("ICE_BEAM", () -> new Attack("Ice Beam", 70, Attack.AttackType.SPECIAL));
        register("POISON_STING", () -> new Attack("Poison Sting", 20, Attack.AttackType.STATUS));
        register("THUNDER", () -> new Attack("Thunder", 90, Attack.AttackType.SPECIAL));
        register("METEORO", () -> new Attack("Meteoro", 120, Attack.AttackType.SPECIAL));
        register("CRITICAL_STRIKE", () -> new Attack("Critical Strike", 90, Attack.AttackType.CRITICAL));
    }

    @Override
    public Attack create(String attackName) {
        String normalizedName = normalize(attackName);
        return attackRegistry.getOrDefault(normalizedName, () -> DEFAULT_ATTACK).get();
    }

    public void register(String attackName, Supplier<Attack> supplier) {
        attackRegistry.put(normalize(attackName), supplier);
    }

    private String normalize(String attackName) {
        return attackName != null ? attackName.toUpperCase() : "";
    }
}

package com.taller.patrones.domain;

/**
 * Representa un ataque que puede ejecutar un personaje.
 */
public class Attack {
    private final String name;
    private final int basePower;
    private final AttackType type;

    public Attack(String name, int basePower, AttackType type) {
        this.name = name;
        this.basePower = basePower;
        this.type = type;
    }

    public String getName() { return name; }
    public int getBasePower() { return basePower; }
    public AttackType getType() { return type; }

    public void execute(Character attacker, Character defender, Battle battle, int damage) {
        defender.takeDamage(damage);
        battle.log(attacker.getName() + " usa " + name + " y hace " + damage + " de daño a " + defender.getName());
    }

    public enum AttackType {
        NORMAL, SPECIAL, STATUS, CRITICAL
    }
}

// Composite pattern
public interface AttackComponent {
    void execute(Character attacker, Character defender, Battle battle, CombatEngine engine);
}

// Simple attack as component
class SimpleAttackComponent implements AttackComponent {
    private final Attack attack;

    public SimpleAttackComponent(Attack attack) {
        this.attack = attack;
    }

    @Override
    public void execute(Character attacker, Character defender, Battle battle, CombatEngine engine) {
        int damage = engine.calculateDamage(attacker, defender, attack);
        attack.execute(attacker, defender, battle, damage);
    }
}

// Composite attack
class CompositeAttack implements AttackComponent {
    private final java.util.List<AttackComponent> attacks;

    public CompositeAttack(java.util.List<AttackComponent> attacks) {
        this.attacks = attacks;
    }

    @Override
    public void execute(Character attacker, Character defender, Battle battle, CombatEngine engine) {
        for (AttackComponent ac : attacks) {
            ac.execute(attacker, defender, battle, engine);
        }
    }
}
}

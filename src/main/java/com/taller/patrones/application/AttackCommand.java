package com.taller.patrones.application;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

/**
 * Comando para aplicar un ataque y poder deshacerlo.
 */
public class AttackCommand {
    private final Battle battle;
    private final Character attacker;
    private final Character defender;
    private final Attack attack;
    private int damage;
    private boolean executed = false;

    public AttackCommand(Battle battle, Character attacker, Character defender, Attack attack, int damage) {
        this.battle = battle;
        this.attacker = attacker;
        this.defender = defender;
        this.attack = attack;
        this.damage = damage;
    }

    public void execute() {
        defender.takeDamage(damage);
        battle.log(attacker.getName() + " usa " + attack.getName() + " y hace " + damage + " de daño a " + defender.getName());
        executed = true;
    }

    public void undo() {
        if (executed) {
            defender.heal(damage);
            battle.log("Se deshace el ataque " + attack.getName() + " de " + attacker.getName() + ". Se restaura " + damage + " de vida a " + defender.getName());
            executed = false;
        }
    }

    public Attack getAttack() { return attack; }
    public int getDamage() { return damage; }
}

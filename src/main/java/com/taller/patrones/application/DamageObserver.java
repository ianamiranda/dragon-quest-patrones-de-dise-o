package com.taller.patrones.application;

import com.taller.patrones.domain.Attack;

public interface DamageObserver {
    void onDamage(DamageEvent event);

    class DamageEvent {
        private final Attack attack;
        private final String attackerName;
        private final String defenderName;
        private final int damage;
        private final String battleId;

        public DamageEvent(Attack attack, String attackerName, String defenderName, int damage, String battleId) {
            this.attack = attack;
            this.attackerName = attackerName;
            this.defenderName = defenderName;
            this.damage = damage;
            this.battleId = battleId;
        }

        public Attack getAttack() { return attack; }
        public String getAttackerName() { return attackerName; }
        public String getDefenderName() { return defenderName; }
        public int getDamage() { return damage; }
        public String getBattleId() { return battleId; }
    }
}

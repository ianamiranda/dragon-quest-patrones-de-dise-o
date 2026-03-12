package com.taller.patrones.domain;

/**
 * Representa un personaje en combate.
 */
public class Character {
    public void heal(int amount) {
        this.currentHp = Math.min(maxHp, currentHp + amount);
    }

    private final String name;
    private int currentHp;
    private final int maxHp;
    private final int attack;
    private final int defense;
    private final int speed;

    private Character(Builder builder) {
        this.name = builder.name;
        this.maxHp = builder.maxHp;
        this.currentHp = builder.maxHp;
        this.attack = builder.attack;
        this.defense = builder.defense;
        this.speed = builder.speed;
    }

    public Character(String name, int maxHp, int attack, int defense, int speed) {
        this(builder(name)
                .maxHp(maxHp)
                .attack(attack)
                .defense(defense)
                .speed(speed));
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public String getName() { return name; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }

    public void takeDamage(int damage) {
        this.currentHp = Math.max(0, currentHp - damage);
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public double getHpPercentage() {
        return maxHp > 0 ? (double) currentHp / maxHp * 100 : 0;
    }

    public static class Builder {
        private final String name;
        private int maxHp = 100;
        private int attack = 10;
        private int defense = 10;
        private int speed = 10;

        public Builder(String name) {
            this.name = (name == null || name.isBlank()) ? "Desconocido" : name;
        }

        public Builder maxHp(int maxHp) {
            this.maxHp = Math.max(1, maxHp);
            return this;
        }

        public Builder attack(int attack) {
            this.attack = Math.max(0, attack);
            return this;
        }

        public Builder defense(int defense) {
            this.defense = Math.max(0, defense);
            return this;
        }

        public Builder speed(int speed) {
            this.speed = Math.max(0, speed);
            return this;
        }

        public Character build() {
            return new Character(this);
        }
    }
}

package com.taller.patrones.interfaces.rest.adapter;

import java.util.Map;

/**
 * Adapter para formato fighter1_name, fighter1_hp, fighter2_name, etc.
 */
public class SnakeCaseBattleRequestAdapter implements ExternalBattleRequestAdapter {

    @Override
    public boolean supports(Map<String, Object> body) {
        return true;
    }

    @Override
    public ExternalBattleStartRequest adapt(Map<String, Object> body) {
        String fighter1Name = asString(body.get("fighter1_name"), "Héroe");
        int fighter1Hp = asInt(body.get("fighter1_hp"), 150);
        int fighter1Atk = asInt(body.get("fighter1_atk"), 25);
        String fighter2Name = asString(body.get("fighter2_name"), "Dragón");
        int fighter2Hp = asInt(body.get("fighter2_hp"), 120);
        int fighter2Atk = asInt(body.get("fighter2_atk"), 30);

        return new ExternalBattleStartRequest(
                fighter1Name, fighter1Hp, fighter1Atk,
                fighter2Name, fighter2Hp, fighter2Atk
        );
    }

    private String asString(Object value, String defaultValue) {
        if (value instanceof String s && !s.isBlank()) {
            return s;
        }
        return defaultValue;
    }

    private int asInt(Object value, int defaultValue) {
        if (value instanceof Number n) {
            return n.intValue();
        }
        if (value instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}

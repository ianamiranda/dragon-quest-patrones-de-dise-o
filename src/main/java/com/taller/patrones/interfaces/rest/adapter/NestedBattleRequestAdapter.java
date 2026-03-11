package com.taller.patrones.interfaces.rest.adapter;

import java.util.Collections;
import java.util.Map;

/**
 * Adapter para formato anidado player.health, player.attack, enemy.health.
 */
public class NestedBattleRequestAdapter implements ExternalBattleRequestAdapter {

    @Override
    public boolean supports(Map<String, Object> body) {
        return body.get("player") instanceof Map<?, ?> || body.get("enemy") instanceof Map<?, ?>;
    }

    @Override
    public ExternalBattleStartRequest adapt(Map<String, Object> body) {
        Map<?, ?> player = asMap(body.get("player"));
        Map<?, ?> enemy = asMap(body.get("enemy"));

        String fighter1Name = asString(player.get("name"), "Héroe");
        int fighter1Hp = asInt(player.get("health"), 150);
        int fighter1Atk = asInt(player.get("attack"), 25);

        String fighter2Name = asString(enemy.get("name"), "Dragón");
        int fighter2Hp = asInt(enemy.get("health"), 120);
        int fighter2Atk = asInt(enemy.get("attack"), 30);

        return new ExternalBattleStartRequest(
                fighter1Name, fighter1Hp, fighter1Atk,
                fighter2Name, fighter2Hp, fighter2Atk
        );
    }

    private Map<?, ?> asMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return map;
        }
        return Collections.emptyMap();
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

package com.taller.patrones.interfaces.rest.adapter;

/**
 * DTO normalizado para iniciar batallas desde proveedores externos.
 */
public record ExternalBattleStartRequest(
        String fighter1Name,
        int fighter1Hp,
        int fighter1Atk,
        String fighter2Name,
        int fighter2Hp,
        int fighter2Atk
) {
}

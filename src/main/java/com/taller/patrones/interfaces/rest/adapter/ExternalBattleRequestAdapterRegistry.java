package com.taller.patrones.interfaces.rest.adapter;

import java.util.List;
import java.util.Map;

/**
 * Selecciona el adapter adecuado para cada payload externo.
 */
public class ExternalBattleRequestAdapterRegistry {

    private final List<ExternalBattleRequestAdapter> adapters = List.of(
            new NestedBattleRequestAdapter(),
            new SnakeCaseBattleRequestAdapter()
    );

    public ExternalBattleStartRequest adapt(Map<String, Object> body) {
        Map<String, Object> safeBody = body != null ? body : Map.of();
        for (ExternalBattleRequestAdapter adapter : adapters) {
            if (adapter.supports(safeBody)) {
                return adapter.adapt(safeBody);
            }
        }
        throw new IllegalStateException("No hay adapter configurado para el payload externo");
    }
}

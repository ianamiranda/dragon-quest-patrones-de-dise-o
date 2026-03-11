package com.taller.patrones.interfaces.rest.adapter;

import java.util.Map;

/**
 * Adapter para convertir payloads externos al formato interno esperado.
 */
public interface ExternalBattleRequestAdapter {

    boolean supports(Map<String, Object> body);

    ExternalBattleStartRequest adapt(Map<String, Object> body);
}

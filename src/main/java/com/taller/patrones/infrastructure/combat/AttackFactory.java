package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

/**
 * Contrato para crear ataques por nombre.
 */
public interface AttackFactory {

    Attack create(String attackName);
}

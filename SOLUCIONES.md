
## Ejercicios (enfoque escenario → patrón)

### 1. Añadir un nuevo tipo de ataque

**Situación:** Quieres añadir el ataque "Meteoro" (120 de poder, tipo especial). Abres `CombatEngine` y ves que tanto `createAttack()` como `calculateDamage()` tienen un `switch` que crece con cada ataque o tipo nuevo.

**Preguntas:**
- ¿Qué problema te encuentras al añadir "Meteoro"?
- ¿Qué pasa si mañana piden 10 ataques más?
- ¿Qué patrón permitiría añadir ataques **sin modificar** `CombatEngine`?

**Pista:** Busca en `infrastructure/combat/CombatEngine.java`

#### Solucion: 

1. **¿Qué problema te encuentras al añadir "Meteoro"?**
	El problema es que debes abrir y modificar `CombatEngine` para agregar un nuevo `case` en `createAttack()`. Eso genera alto acoplamiento y rompe el principio de **abierto/cerrado (OCP)**: una clase central cambia cada vez que aparece un ataque nuevo.

2. **¿Qué pasa si mañana piden 10 ataques más?**
	El `switch` crece rápidamente, el código se vuelve más difícil de mantener, aumenta el riesgo de errores (por ejemplo, nombres mal escritos), y varios desarrolladores terminan tocando el mismo archivo, creando más conflictos y regresiones.

3. **¿Qué patrón permitiría añadir ataques sin modificar `CombatEngine`?**
	El patrón adecuado es **Factory** (factoría). En esta solución, `CombatEngine` delega la creación en una interfaz `AttackFactory`, y la implementación `DefaultAttackFactory` usa un registro de ataques. Así, para añadir "Meteoro" o cualquier otro ataque, solo se registra en la factoría sin cambiar la lógica del motor.


---

### 2. Añadir una nueva fórmula de daño

**Situación:** Los ataques de tipo STATUS (veneno, parálisis) no deberían hacer daño directo. Pero en `calculateDamage()` el case STATUS devuelve `attacker.getAttack()` — algo no cuadra.

Además, te piden un nuevo tipo: "CRÍTICO", con fórmula `daño * 1.5` y 20% de probabilidad.

**Preguntas:**
- ¿Qué principio SOLID se viola al añadir otro `case` en el switch?
- ¿Qué patrón permitiría tener fórmulas de daño intercambiables sin tocar el código existente?

**Pista:** Cada tipo de ataque (NORMAL, SPECIAL, STATUS) tiene una fórmula distinta.

#### Solucion:

1. **¿Qué principio SOLID se viola al añadir otro `case` en el switch?**
	Se viola el principio de **abierto/cerrado (OCP)**, porque cada vez que aparece una fórmula nueva (por ejemplo CRITICAL) hay que modificar `CombatEngine`. También se compromete la responsabilidad única, ya que el motor termina concentrando demasiadas reglas de negocio.

2. **¿Qué patrón permitiría tener fórmulas de daño intercambiables sin tocar el código existente?**
	El patrón adecuado es **Strategy**. Cada fórmula se encapsula en una clase (`NormalDamageStrategy`, `SpecialDamageStrategy`, `StatusDamageStrategy`, `CriticalDamageStrategy`) y `CombatEngine` solo selecciona la estrategia según el tipo de ataque. Así se pueden añadir nuevas fórmulas registrando otra estrategia, sin reescribir el motor.

---

### 3. Crear personajes con muchas estadísticas

**Situación:** En `BattleService.startBattle()` creas personajes así:

```java
Character player = new Character("Héroe", 150, 25, 15, 20);
```

Ahora necesitas soportar: equipamiento, buffos temporales, clase (guerrero/mago). El constructor de `Character` empieza a tener 10+ parámetros. Algunos son opcionales.

**Preguntas:**
- ¿Qué problema tiene un constructor con muchos parámetros?
- ¿Cómo harías para que `new Character(...)` sea legible cuando hay valores por defecto?
- ¿Qué patrón permite construir objetos complejos paso a paso?

**Pista:** Mira cómo se crean los personajes en `BattleService` y en el endpoint `/start/external`.

#### Solucion:

1. **¿Qué problema tiene un constructor con muchos parámetros?**
	Un constructor largo vuelve el código difícil de leer y propenso a errores, porque es fácil confundir el orden de los argumentos. Además, cuando hay parámetros opcionales, aparecen combinaciones incómodas (valores "relleno" o sobrecargas excesivas).

2. **¿Cómo harías para que `new Character(...)` sea legible cuando hay valores por defecto?**
	Usaría una construcción fluida indicando solo lo necesario y dejando el resto con valores por defecto, por ejemplo: `Character.builder("Héroe").maxHp(150).attack(25).build();`. Así se entiende qué representa cada valor sin depender del orden posicional.

3. **¿Qué patrón permite construir objetos complejos paso a paso?**
	El patrón correcto es **Builder**. Encapsula la construcción del objeto en pasos (`maxHp`, `attack`, `defense`, `speed`, etc.) y finaliza con `build()`, manteniendo el código legible, extensible y fácil de evolucionar cuando el modelo crezca.

---

### 4. Un único almacén de batallas

**Situación:** `BattleRepository` usa un `Map` estático para que funcione. Pero `BattleService` hace `new BattleRepository()` cada vez. Si otro equipo crea un `TournamentService` que también hace `new BattleRepository()`, ¿compartirían las batallas?

**Preguntas:**
- ¿Qué pasaría si dos clases crean su propio `BattleRepository` sin el `static`?
- ¿Cómo asegurar que **toda la aplicación** use la misma instancia de almacenamiento?
- ¿Qué patrón garantiza una única instancia de una clase?

**Pista:** `infrastructure/persistence/BattleRepository.java`

#### Solucion:

1. **¿Qué pasaría si dos clases crean su propio `BattleRepository` sin el `static`?**
	Cada clase tendría su propio almacén en memoria y las batallas no se compartirían. Por ejemplo, una batalla creada desde `BattleService` no aparecería en otro servicio que use otra instancia distinta.

2. **¿Cómo asegurar que toda la aplicación use la misma instancia de almacenamiento?**
	Haciendo que `BattleRepository` no se pueda instanciar libremente (constructor privado) y exponiendo un único punto de acceso (`getInstance()`). Así, todos los servicios trabajan contra el mismo repositorio en memoria.

3. **¿Qué patrón garantiza una única instancia de una clase?**
	El patrón es **Singleton**. Se usa cuando una clase debe existir una sola vez en todo el sistema y ser compartida por todos los consumidores.

---

### 5. Recibir datos de un API externo

**Situación:** El endpoint `POST /api/battle/start/external` recibe JSON con campos `fighter1_hp`, `fighter1_atk`, `fighter2_name`, etc. El controller hace el mapeo manual a `Character` y `Battle`.

Mañana llega otro proveedor con formato distinto: `player.health`, `player.attack`, `enemy.health`...

**Preguntas:**
- ¿Qué problema hay en poner la lógica de conversión en el controller?
- ¿Cómo aislar la conversión "formato externo → nuestro dominio" para no ensuciar el controller?
- ¿Qué patrón permite que un objeto "adaptado" se use como si fuera uno de los nuestros?

**Pista:** `interfaces/rest/BattleController.java` — método `startBattleFromExternal`

#### Solucion:

1. **¿Qué problema hay en poner la lógica de conversión en el controller?**
	El controller queda acoplado a formatos externos concretos y crece con reglas de mapeo que no son su responsabilidad. Cada nuevo proveedor obliga a tocar el endpoint, aumentando riesgo de errores y dificultando pruebas.

2. **¿Cómo aislar la conversión "formato externo → nuestro dominio" para no ensuciar el controller?**
	Creando una capa dedicada de adaptación (adapters) que reciba el payload externo y lo convierta a un DTO interno normalizado. Así el controller solo delega y mantiene una responsabilidad clara.

3. **¿Qué patrón permite que un objeto "adaptado" se use como si fuera uno de los nuestros?**
	El patrón es **Adapter**. Permite envolver distintos formatos externos y exponer una interfaz común para el resto de la aplicación.

---

### 6. Notificar cuando ocurre daño

**Situación:** Necesitas:
- Enviar un evento a un sistema de analytics cada vez que hay daño
- Escribir en un log de auditoría
- Actualizar estadísticas en tiempo real

Ahora mismo solo existe `battle.log()`. Tendrías que añadir código en `BattleService.applyDamage()` para cada uno de estos casos.

**Preguntas:**
- ¿Qué pasa si añades 5 "suscriptores" más? ¿Cuántas líneas tocarías en `applyDamage()`?
- ¿Cómo desacoplar "ejecutar ataque" de "notificar a quien le interese"?
- ¿Qué patrón permite que varios objetos reaccionen a un evento sin que el emisor los conozca?

**Pista:** El método `applyDamage` en `BattleService` es el único que sabe cuándo hay daño.

#### Solución:

1. **¿Qué pasa si añades 5 "suscriptores" más?**
	El método `applyDamage()` crecería mucho, tendrías que modificarlo cada vez que quieras notificar a un nuevo sistema, lo que hace el código difícil de mantener y viola el principio de abierto/cerrado (OCP).

2. **¿Cómo desacoplar "ejecutar ataque" de "notificar a quien le interese"?**
	Aplicando el patrón **Observer**. Se define una interfaz `DamageObserver` con un método `onDamage(DamageEvent event)`. Cada clase interesada (analytics, auditoría, estadísticas, etc.) implementa esta interfaz y se registra como observador en `BattleService`. Cuando ocurre daño, `BattleService` notifica a todos los observadores sin saber quiénes son ni qué hacen.

3. **¿Qué patrón permite que varios objetos reaccionen a un evento sin que el emisor los conozca?**
	El patrón es **Observer**. Permite que varios objetos se suscriban a eventos y reaccionen cuando estos ocurren, sin acoplar el emisor a los receptores.

**Ventajas:**
- Puedes agregar o quitar observadores sin modificar el código central.
- El emisor (BattleService) no necesita saber quién está escuchando.
- El sistema es extensible y desacoplado.

**Implementación en el código:**
- Se creó la interfaz `DamageObserver` y la clase interna `DamageEvent`.
- `BattleService` mantiene una lista de observadores y los notifica en el método `applyDamage()`.
- Para agregar un nuevo suscriptor, solo hay que implementar la interfaz y registrarlo, sin tocar la lógica principal.

---

### 7. Deshacer el último ataque

**Situación:** Quieren la funcionalidad "Deshacer" — revertir el último ataque ejecutado.

Ahora el ataque se ejecuta directamente en `applyDamage()`. No hay registro de "qué se hizo".

**Preguntas:**
- ¿Qué tendrías que cambiar para poder "deshacer"?
- ¿Cómo encapsular una acción (ataque) para poder ejecutarla, guardarla y revertirla?
- ¿Qué patrón trata las acciones como objetos de primera clase?

**Pista:** La lógica del ataque está en `BattleService.applyDamage()`.

---

### 8. Simplificar la API del combate

**Situación:** Para ejecutar un ataque, el controller llama a `battleService.executePlayerAttack()` o `executeEnemyAttack()`, que a su vez usa `CombatEngine`, aplica daño, cambia turno, etc. Un cliente externo que quiera integrarse tendría que conocer `BattleService`, `CombatEngine`, `BattleRepository`...

**Preguntas:**
- ¿Qué problema hay en exponer muchos detalles internos a quien solo quiere "hacer un ataque"?
- ¿Qué patrón ofrece una interfaz simple que oculta la complejidad del subsistema?

**Pista:** Piensa en qué necesita saber un cliente para ejecutar un ataque.

---

### 9. Ataques compuestos (combo)

**Situación:** Quieres un ataque "Combo Triple" que ejecuta Tackle + Slash + Fireball en secuencia.

Ahora cada ataque es independiente. No hay forma de agrupar varios.

**Preguntas:**
- ¿Cómo representar "un ataque que son varios ataques"?
- ¿Qué patrón permite tratar un grupo de objetos igual que un objeto individual?

**Pista:** `Attack` es una unidad. ¿Cómo hacer que varios `Attack` se comporten como uno?

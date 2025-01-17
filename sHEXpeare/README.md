# sHEXpeare - README

Este proyecto implementa un jugador automático para el juego **HEX**, utilizando algoritmos avanzados de búsqueda y estrategias heurísticas para optimizar las decisiones del jugador. A continuación, se describen los componentes principales del proyecto:

---

## Minimax

El algoritmo **Minimax** se utiliza para evaluar posibles movimientos y determinar el más óptimo. Este algoritmo está diseñado para jugar de forma competitiva en un entorno adversarial como HEX. Se implementa de dos formas:

1. **Minimax Iterativo**:
   - Explora progresivamente niveles de profundidad creciente dentro de un tiempo límite predefinido (10 segundos en este caso).
   - Cuando el tiempo expira, devuelve el mejor movimiento encontrado hasta ese momento.
   - **Ventajas**:
     - Adaptación a restricciones de tiempo.
     - Retorna decisiones razonables incluso en profundidades parciales.

2. **Minimax No Iterativo**:
   - Explora directamente hasta la profundidad especificada sin considerar restricciones de tiempo.
   - **Ventajas**:
     - Examina toda la profundidad definida, asegurando análisis exhaustivo.
   - **Desventajas**:
     - Incremento exponencial en el tiempo requerido a medida que aumenta la profundidad.

### Comparativa
- Con una profundidad fija de 4:
  - **Minimax No Iterativo**: Exploró 2,667,576 nodos en 220.9 segundos.
  - **Minimax Iterativo**: Exploró 105,193 nodos hasta la profundidad 3 en solo 10 segundos.
- **Conclusión**: El Minimax Iterativo es más adecuado para contextos con limitaciones temporales.

---

## Poda Alfa-Beta

La implementación incluye optimización mediante **poda alfa-beta**, que mejora significativamente la eficiencia del algoritmo al:
- Reducir el número de nodos explorados.
- Mantener la calidad de las decisiones tomadas.

### Comparativa
- **Sin poda**:
  - El número de nodos explorados crece exponencialmente, utilizando más recursos.
- **Con poda**:
  - Disminuye considerablemente la exploración innecesaria.
  - Optimiza el tiempo sin comprometer la calidad del movimiento.

---

## Heurísticas

Las heurísticas guían las decisiones del jugador considerando factores estratégicos específicos de HEX. Se implementaron heurísticas personalizadas para los dos jugadores:

### PLAYER1
- Prioriza la construcción de conexiones y rutas óptimas desde el inicio.
- Basado en el algoritmo de **Dijkstra**, asigna diferentes pesos a las casillas:
  - **Casillas ocupadas por el jugador**: Peso 0.
  - **Casillas vacías estratégicas**:
    - Construcción de puentes: Peso 5.
    - Evasiones diagonales: Peso 4.
    - Bloqueos: Peso 2.
  - **Casillas del oponente**: Penalización alta.
- **Estrategias principales**:
  - Completar conexiones existentes.
  - Explorar nuevas rutas estratégicas.

### PLAYER2
- Equilibra la creación de conexiones y el bloqueo de estrategias del oponente.
- Adapta pesos y prioridades para maximizar el impacto defensivo:
  - Interrupción de caminos críticos de PLAYER1.
  - Optimización de rutas propias considerando bloqueos estratégicos.

### Diferencias Clave
- **Iniciativa**: PLAYER1 construye y consolida desde el principio; PLAYER2 equilibra creación y defensa.
- **Ajustes de pesos**: PLAYER2 asigna más prioridad a bloquear caminos del oponente.

---

## Resultados

- Las heurísticas personalizadas han mostrado mejoras significativas:
  - **PLAYER1**: Consistente en sus victorias gracias a su estrategia optimizada.
  - **PLAYER2**: Incrementó su tasa de victorias de 10% a 80% con heurísticas personalizadas.

---

## Cómo usar

1. Ajusta los parámetros en el constructor del jugador:
   - `depth`: Profundidad de búsqueda.
   - `iterative`: Modo iterativo (`true`) o no iterativo (`false`).
   - `alphabeta`: Activar o desactivar poda alfa-beta.
2. Ejecuta el jugador en una partida de HEX para evaluar su rendimiento.

---

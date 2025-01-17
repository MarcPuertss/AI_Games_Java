# Conecta 4 AI

## Descripción del Proyecto
Este jugador implementado es un agente de inteligencia artificial desarrollado para jugar al Conecta 4 utilizando el algoritmo **Minimax con poda Alfa-Beta** y una heurística avanzada basada en la evaluación de líneas de fichas en el tablero.

El objetivo principal del agente es maximizar sus oportunidades de victoria mientras bloquea activamente los intentos del oponente de completar una línea de cuatro fichas.

---

## **Heurística Utilizada**
### **Evaluación de Estados del Tablero**
La heurística asigna un valor numérico a cada estado del tablero en función de:

1. **Líneas de fichas propias**: Se otorgan puntos a las líneas con fichas propias consecutivas.
   - Cada ficha propia consecutiva suma **2 puntos**.
   - Se priorizan líneas en construcción para acercarse a la victoria.

2. **Líneas de fichas rivales**: Se penalizan líneas con fichas del oponente.
   - Cada ficha rival consecutiva resta **1 punto**.
   - Se evitan jugadas que permitan la victoria inmediata del rival.

3. **Espacios libres**: Se valoran los espacios abiertos en una línea que pueden ser utilizados para completar una secuencia.
   - Cada espacio vacío suma **3 puntos**.

4. **Resultado final de la heurística**: Se calcula la diferencia entre la puntuación de las líneas propias y la de las líneas rivales para determinar qué tan favorable es la posición para el jugador.
   
   **Fórmula general:**
   ```
   Heurística = (Puntos de líneas propias) - (Puntos de líneas rivales)
   ```
   
   Un valor positivo favorece al jugador, mientras que un valor negativo indica ventaja para el oponente.

---

## **Algoritmo Minimax con Poda Alfa-Beta**
### **Optimización de la Exploración de Movimientos**
Para mejorar el rendimiento, el agente utiliza **Minimax con poda Alfa-Beta**, que reduce drásticamente el número de nodos explorados.

1. **Sin poda Alfa-Beta**:
   - Se exploran todas las ramas posibles hasta la profundidad máxima.
   - El crecimiento exponencial de los nodos explorados hace que el tiempo de cálculo aumente considerablemente.
   - En Conecta 4, esto puede traducirse en millones de nodos explorados.

2. **Con poda Alfa-Beta**:
   - Se introducen dos límites: `Alpha` (valor máximo garantizado para Max) y `Beta` (valor mínimo garantizado para Min).
   - Se descartan ramas innecesarias cuando se detecta que no pueden afectar el resultado final.
   - La reducción del número de nodos explorados puede llegar a un **60-70%**, mejorando significativamente la eficiencia.

### **Impacto del Orden de Exploración**
- Si las mejores jugadas se exploran primero, la poda ocurre más rápido, reduciendo aún más los cálculos.
- En el código, la función `obtenerCol()` evalúa las columnas del tablero de manera secuencial.
  - Esto significa que el orden de exploración es clave para optimizar la poda y acelerar el tiempo de decisión.

---

## **Resultados y Conclusiones**
- La implementación de la heurística permite evaluar con precisión el estado del juego y priorizar movimientos estratégicos.
- La combinación de **Minimax con poda Alfa-Beta** ha reducido significativamente el tiempo de cálculo sin afectar la calidad de las decisiones.
- Optimizar el orden de exploración de los movimientos mejora aún más la eficiencia del algoritmo.

A la vez es capaz de jugar de manera competitiva contra diferentes estrategias y niveles de oponentes, mostrando una toma de decisiones óptima y eficiente.

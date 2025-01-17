# AI_Games_Java

## Descripción del Proyecto
AI_Games_Java es una colección de implementaciones de inteligencia artificial para juegos de estrategia clásicos en Java. Este repositorio incluye agentes basados en algoritmos avanzados de búsqueda y heurísticas optimizadas para juegos como **Robocode, Conecta 4 y HEX**.

Cada uno de estos proyectos utiliza técnicas como **Minimax con poda Alfa-Beta**, **heurísticas avanzadas** y **estrategias adaptativas** para mejorar el rendimiento y la toma de decisiones en entornos competitivos.

---

## **Proyectos Incluidos**

### **1. FollowTheLeaderTeam (Robocode)**
#### **Descripción**
Un equipo de robots en Robocode diseñado para operar en conjunto mediante una **jerarquía dinámica**, donde un líder dirige los movimientos y ataques.

#### **Características Principales**
- **Jerarquía Dinámica**: Reasigna roles cuando el líder es destruido.
- **Movimiento en Formación**: Seguimiento del líder con algoritmos de posicionamiento.
- **Disparo Coordinado**: Sincronización de ataques basada en mensajes de broadcast.
- **Esquiva de Obstáculos**: Algoritmos de evasión para evitar colisiones con robots y paredes.

---

### **2. Fumate4 (Conecta 4 AI)**
#### **Descripción**
Un jugador de Conecta 4 basado en **Minimax con poda Alfa-Beta** y una **heurística de evaluación de líneas**.

#### **Características Principales**
- **Heurística basada en evaluación de líneas**:
  - Puntos por fichas propias consecutivas (+2).
  - Penalización por fichas rivales (-1).
  - Bonus por espacios vacíos estratégicos (+3).
- **Minimax con poda Alfa-Beta**:
  - Reducción del 60-70% en nodos explorados.
  - Optimización del tiempo de decisión.
- **Impacto del orden de exploración**:
  - Priorización de mejores jugadas para mejorar la poda.

---

### **3. sHEXpeare (HEX AI)**
#### **Descripción**
Un jugador automático para el juego HEX utilizando **Minimax Iterativo**, **Poda Alfa-Beta** y **heurísticas avanzadas basadas en Dijkstra**.

#### **Características Principales**
- **Minimax Iterativo vs No Iterativo**:
  - Modo iterativo ajustable con límite de tiempo.
  - Reducción del tiempo de cálculo sin comprometer la calidad.
- **Optimización con poda Alfa-Beta**:
  - Disminución drástica de nodos explorados.
  - Mejor rendimiento computacional.
- **Heurísticas personalizadas**:
  - PLAYER1: Basado en **Dijkstra**, prioriza conexiones.
  - PLAYER2: Equilibra bloqueo de caminos y expansión estratégica.

---

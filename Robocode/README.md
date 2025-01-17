# Robocode - README

## Descripción del Proyecto
1. TimidinRobot es un robot desarrollado para Robocode que sigue una estrategia basada en una máquina de estados con tres fases definidas. Su objetivo es detectar un enemigo, moverse a una esquina estratégica y mantener una defensa activa desde allí 
2. Este proyecto implementa un equipo de robots en **Robocode** llamado FollowTheLeaderTeam diseñado para operar en equipo bajo una jerarquía dinámica, con el objetivo de seguir a un líder y coordinarse para combatir eficazmente contra enemigos.
---

## Estrategia de Comportamiento TimidinRobot
### **Fase 0: Detección del Enemigo y Cálculo de la Esquina**
1. El radar gira hasta detectar un enemigo.
2. Se calculan las coordenadas del enemigo y se determina la esquina más alejada del campo de batalla.
3. El robot se dirige hacia esta esquina para evitar el combate directo.

### **Fase 1: Movimiento y Esquiva de Obstáculos**
- El robot avanza en línea recta hacia la esquina seleccionada.
- Detecta y esquiva obstáculos como otros robots o paredes.
- Si encuentra un obstáculo, dispara para despejar el camino o ajusta su trayectoria con maniobras de esquiva.
- Utiliza un algoritmo de esquiva basado en giros de 90º y movimientos cortos hasta retomar su trayectoria.

#### **Reacción a Colisiones**
- **Colisión con la pared**: Retrocede 50 unidades y gira 90º.
- **Colisión con otro robot**: Activa la maniobra de esquiva inmediatamente.

### **Fase 2: Posición Defensiva y Ataque**
- Una vez en la esquina, el robot entra en modo defensivo.
- Gira constantemente el radar para detectar enemigos.
- Ajusta el cañón y dispara con potencia proporcional a la distancia del enemigo.

---

## Funcionalidades Principales FollowTheLeaderTeam

### **1. Jerarquía Dinámica**
- En la fase inicial, los robots determinan un líder de forma aleatoria basándose en la posición X del campo de batalla.
- Los roles (líder, segundo, tercero, etc.) se asignan en función de la distancia al líder.
- Si el líder es destruido, la jerarquía se actualiza dinámicamente.

### **2. Movimiento en Conga**
- Los robots siguen a su predecesor en una formación organizada utilizando mensajes de broadcast.
- El líder sigue una ruta rectangular predefinida alrededor del campo de batalla:
  - **Esquivar obstáculos**: Implementado mediante maniobras de esquiva para evitar colisiones con robots o paredes.
  - **Cambio de sentido y roles**: Cada 15 segundos, los roles y el sentido de rotación cambian para mantener la dinámica del equipo.

### **3. Disparo Coordinado**
- Los robots seguidores disparan al enemigo detectado por el líder al llegar a la siguiente esquina del recorrido.
- Los mensajes de broadcast informan la posición del enemigo, asegurando una respuesta sincronizada.

### **4. Gestión de Eventos**
- **Muerte del líder**: El rol de líder se reasigna automáticamente al robot más cercano.
- **Esquiva de obstáculos**: Se ejecutan maniobras predefinidas en 7 pasos para evitar colisiones.
- **Reversión de la formación**: Los roles se invierten y los robots adaptan sus movimientos para mantener la cohesión.

---

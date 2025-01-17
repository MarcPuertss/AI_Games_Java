
package edu.upc.epsevg.prop.hex.players;


import edu.upc.epsevg.prop.hex.*;

import java.awt.Point;
import java.util.*;
/**
 * Classe que implementa la heurística específica per al jugador PLAYER1 en el joc HEX, 
 * utilitzant l'algorisme de Dijkstra per calcular la distància mínima entre els extrems 
 * del tauler. Aquesta heurística considera diferents estratègies i pesos per maximitzar 
 * les oportunitats de connectar els costats oposats del jugador.
 * 
 * Aquesta implementació assigna pesos a les caselles del tauler en funció de la seva utilitat 
 * estratègica, identificant oportunitats per construir ponts, evadir bloquejos diagonals o completar
 * connexions.
 * 
 * @author Eric Millan and Marc Puertas
 */
public class Heuristica_Player1 {
    
    
    /**
     * Calcula la distància mínima entre els extrems del tauler per al jugador PLAYER1 
     * mitjançant l'algorisme de Dijkstra.
     *
     * @param gameState L'estat actual del joc HEX.
     * @param player El tipus de jugador (PLAYER1 o PLAYER2).
     * @return La distància mínima entre els extrems del tauler per al jugador PLAYER1.
     */
    public static int calcularDistanciaDijkstra(HexGameStatus gameState, PlayerType player) {
        int size = gameState.getSize();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.distancia));
        boolean[][] visitado = new boolean[size][size];
        int[][] distancias = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                distancias[i][j] = Integer.MAX_VALUE;
                visitado[i][j] = false;
            }
        }
        
        Point nodoInicial = new Point(1, 9);
        int nodeInicial = peso(gameState, nodoInicial, player);
        distancias[1][9] = nodeInicial;
        queue.add(new Node(nodoInicial, nodeInicial));

        if (player == PlayerType.PLAYER1) {
            for (int y = 0; y < size; y++) {
                Point puntoInicial = new Point(0, y);
                int costoInicial = peso(gameState, puntoInicial, player);
                distancias[0][y] = costoInicial;  
                queue.add(new Node(puntoInicial, costoInicial));
            }
        } else {
            for (int x = 0; x < size; x++) {
                Point puntoInicial = new Point(x, 0);
                int costoInicial = peso(gameState, puntoInicial, player);
                distancias[x][0] = costoInicial;  
                queue.add(new Node(puntoInicial, costoInicial));
            }
        }

        while (!queue.isEmpty()) {
            Node actual = queue.poll();
            Point punto = actual.punto;

            if (visitado[punto.x][punto.y]) continue;
            visitado[punto.x][punto.y] = true;

            if ((player == PlayerType.PLAYER1 && punto.x == size - 1) || 
                (player == PlayerType.PLAYER2 && punto.y == size - 1)) {
                return actual.distancia;
            }

            for (Point vecino : gameState.getNeigh(punto)) {
                if (visitado[vecino.x][vecino.y]) continue;

                int nuevaDistancia = actual.distancia + peso(gameState, vecino, player);
                if (nuevaDistancia < distancias[vecino.x][vecino.y]) {
                    distancias[vecino.x][vecino.y] = nuevaDistancia;
                    queue.add(new Node(vecino, nuevaDistancia));
                }
            }
        }

        return Integer.MAX_VALUE; 
    }

    /**
     * Assigna un pes a una casella determinada en funció de la seva utilitat estratègica.
     *
     * @param gameState L'estat actual del joc.
     * @param punto El punt actual que s'està avaluant.
     * @param player El jugador actual.
     * @return El pes de la casella.
     */
    public static int peso(HexGameStatus gameState, Point punto, PlayerType player) {
        int cellType = gameState.getPos(punto);

        if (cellType == PlayerType.getColor(player)) {
            return 0; 
        } else if (cellType == 0) {

            if (esEvasionDiagonal(gameState, punto, player)) {
                return 4;
            } else if (esCompletarPuente(gameState, punto, player)) {
                return 2;
            } else if (esConstruirPuente(gameState, punto, player)) {
                return 5;
            }
            return 7;
        } else {
            return Integer.MAX_VALUE / 2;
        }
    }

    /**
     * Determina si una casella representa una oportunitat d'evadir un bloqueig diagonal.
     *
     * @param gameState L'estat actual del joc.
     * @param punto La casella avaluada.
     * @param player El jugador actual.
     * @return True si és una oportunitat d'evasió diagonal, false altrament.
     */
    public static boolean esEvasionDiagonal(HexGameStatus gameState, Point punto, PlayerType player) {
        List<Point> vecinos = gameState.getNeigh(punto);

        for (Point vecino : vecinos) {
            if (gameState.getPos(vecino) == PlayerType.getColor(player)) {
                if (esDireccionDiagonal(gameState, punto, vecino, player) && !esBloqueadoPorOponente(gameState, punto, vecino, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Comprova si un moviment segueix una direcció diagonal vàlida per al jugador actual.
     *
     * @param gameState L'estat actual del joc.
     * @param actual La posició actual.
     * @param vecino La posició del veí.
     * @param player El jugador actual.
     * @return True si la direcció és diagonal i vàlida, false altrament.
     */    
    public static boolean esDireccionDiagonal(HexGameStatus gameState, Point actual, Point vecino, PlayerType player) {
        int size = gameState.getSize();

        if (player == PlayerType.PLAYER1) {
            return actual.x < vecino.x && vecino.x < size; 
        } else {
            return actual.y < vecino.y && vecino.y < size; 
        }
    }

    /**
     * Comprova si un moviment està bloquejat per l'oponent.
     *
     * @param gameState L'estat actual del joc.
     * @param punto La posició del moviment actual.
     * @param vecino El veí del moviment actual.
     * @param player El jugador actual.
     * @return True si està bloquejat, false altrament.
     */
    public static boolean esBloqueadoPorOponente(HexGameStatus gameState, Point punto, Point vecino, PlayerType player) {
        List<Point> vecinosVecino = gameState.getNeigh(vecino);

        for (Point vecinoDelVecino : vecinosVecino) {
            if (gameState.getPos(vecinoDelVecino) == PlayerType.getColor(PlayerType.opposite(player))) {
                return true; 
            }
        }
        return false;
    }

     /**
     * Determina si un moviment compleix les condicions per completar un pont.
     *
     * @param gameState L'estat actual del joc.
     * @param punto El moviment avaluat.
     * @param player El jugador actual.
     * @return True si compleix les condicions, false altrament.
     */   
    public static boolean esCompletarPuente(HexGameStatus gameState, Point punto, PlayerType player) {
        List<Point> vecinos = gameState.getNeigh(punto);
        int propias = 0, bloqueadas = 0;

        for (Point vecino : vecinos) {
            int vecinoTipo = gameState.getPos(vecino);
            if (vecinoTipo == PlayerType.getColor(player)) {
                propias++;
            } else if (vecinoTipo == PlayerType.getColor(PlayerType.opposite(player))) {
                bloqueadas++;
            }
        }
        return propias == 1 && bloqueadas >= 1 && conectaLados(gameState, punto, player);
    }

    /**
     * Determina si un moviment ajuda a connectar els costats oposats del tauler.
     *
     * @param gameState L'estat actual del joc.
     * @param punto El moviment avaluat.
     * @param player El jugador actual.
     * @return True si ajuda a connectar els costats, false altrament.
     */
    public static boolean conectaLados(HexGameStatus gameState, Point punto, PlayerType player) {
        int size = gameState.getSize();

        if (player == PlayerType.PLAYER1) {
            return punto.x == 0 || punto.x == size - 1;
        } else {
            return punto.y == 0 || punto.y == size - 1;
        }
    }

    /**
     * Determina si un moviment és adequat per construir un pont.
     *
     * @param gameState L'estat actual del joc.
     * @param punto El moviment avaluat.
     * @param player El jugador actual.
     * @return True si és adequat, false altrament.
     */    
    public static boolean esConstruirPuente(HexGameStatus gameState, Point punto, PlayerType player) {
        List<Point> vecinos = gameState.getNeigh(punto);
        int propias = 0;

        for (Point vecino : vecinos) {
            if (gameState.getPos(vecino) == PlayerType.getColor(player)) {
                propias++;
            }
        }

        return propias >= 2 && tieneOpcionesFuturas(gameState, punto, player);
    }

    /**
     * Comprova si un moviment proporciona opcions futures al jugador.
     *
     * @param gameState L'estat actual del joc.
     * @param punto El moviment avaluat.
     * @param player El jugador actual.
     * @return True si proporciona opcions futures, false altrament.
     */
    public static boolean tieneOpcionesFuturas(HexGameStatus gameState, Point punto, PlayerType player) {
        List<Point> vecinos = gameState.getNeigh(punto);
        for (Point vecino : vecinos) {
            if (gameState.getPos(vecino) == 0) { 
                return true;
            }
        }
        return false;
    }
}

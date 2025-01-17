
package edu.upc.epsevg.prop.hex.players;

import edu.upc.epsevg.prop.hex.*;

import java.awt.Point;
import java.util.*;
/**
 * Aquesta classe implementa la heurística utilitzada pel jugador 2 en el joc Hex.
 * Basant-se en l'algorisme de Dijkstra, calcula la distància mínima entre els punts
 * inicials i finals del tauler per determinar l'estratègia més efectiva per guanyar.
 *
 * Aquesta heurística té en compte factors com la construcció de ponts,
 * l'evitació de diagonals bloquejades i altres tàctiques per optimitzar la jugada.
 * 
 * 
 * @author Marc Puertas and Eric Millan
 */
public class Heuristica_Player2 {
    
    /**
     * Calcula la distància mínima entre les regions inicials i finals del tauler
     * per al jugador especificat utilitzant l'algorisme de Dijkstra.
     *
     * @param gameState L'estat actual del joc.
     * @param player El jugador per al qual es calcula la distància (PLAYER2).
     * @return La distància mínima entre les regions inicials i finals.
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
     * Determina el cost associat a un punt concret del tauler en funció de 
     * l'estat de la cel·la i la situació estratègica.
     *
     * @param gameState L'estat actual del joc.
     * @param punto El punt a avaluar.
     * @param player El jugador en qüestió.
     * @return El cost associat a aquest punt.
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
            }
            return 6; 
        } else {
            return Integer.MAX_VALUE / 2;
        }
    }

    /**
     * Verifica si una cel·la representa una evasió diagonal estratègica 
     * per al jugador especificat.
     *
     * @param gameState L'estat actual del joc.
     * @param punto La cel·la a avaluar.
     * @param player El jugador en qüestió.
     * @return Cert si és una evasió diagonal, fals altrament.
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

    public static boolean esDireccionDiagonal(HexGameStatus gameState, Point actual, Point vecino, PlayerType player) {
        int size = gameState.getSize();

        if (player == PlayerType.PLAYER1) {
            return actual.x < vecino.x && vecino.x < size; 
        } else {
            return actual.y < vecino.y && vecino.y < size; 
        }
    }


    public static boolean esBloqueadoPorOponente(HexGameStatus gameState, Point punto, Point vecino, PlayerType player) {
        List<Point> vecinosVecino = gameState.getNeigh(vecino);

        for (Point vecinoDelVecino : vecinosVecino) {
            if (gameState.getPos(vecinoDelVecino) == PlayerType.getColor(PlayerType.opposite(player))) {
                return true; 
            }
        }
        return false;
    }

    
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
     * Verifica si un moviment connecta dues regions del tauler, 
     * com els extrems inicials i finals.
     *
     * @param gameState L'estat actual del joc.
     * @param punto La cel·la a avaluar.
     * @param player El jugador en qüestió.
     * @return Cert si el moviment connecta els extrems, fals altrament.
     */
    public static boolean conectaLados(HexGameStatus gameState, Point punto, PlayerType player) {
        int size = gameState.getSize();

        if (player == PlayerType.PLAYER1) {
            return punto.x == 0 || punto.x == size - 1;
        } else {
            return punto.y == 0 || punto.y == size - 1;
        }
    }
}
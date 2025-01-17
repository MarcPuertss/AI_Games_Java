/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.hex;

import edu.upc.epsevg.prop.hex.players.Heuristica_Player2;
import edu.upc.epsevg.prop.hex.players.sHEXpeare;


/**
 *
 * @author bernat
 */
public class UnitTesting {

    
    public static void main(String[] args) {
    
        
byte[][] board = {
    { 0,  0,  0,  0,  0,  0,  0,  0,  0, 0, 0},
      { 0,  0, 0,  0,  0,   0,   0,  0,  -1, 0, 1},
        { 0, 0,  0,   0,   0,   0,  0,  -1,  0, 0, 1},
          { 0,  0,  0,   0,   0,   0,  -1,  0,  0, 0, 1},
            { 0,  0,  0,   0,   0,   0,  0,  0,  0, 0, 0},
              { 0,  0,  0,   0,   0,   -1,  0,  0,  0, 0, 1},
                { 0,  0,  0,   0,   0,   0,  0,  0,  0, 0, 0},
                 { 0,  0,  0,   -1,   0,   0,  0,  0,  0, 0, 1},
                    { 0,  0,  -1,   0,   0,   0,  0,  0,  0, 0, 1},
                      { 0,  -1,  0,   0,   0,   0,  0,  0,  0, 0, 1},
                        { 0,  0,  0,   0,   0,   0,  0,  0,  0, 0, 1}
};

        HexGameStatus gs = new HexGameStatus(board, PlayerType.PLAYER1); 
        heuristicaDijkstra(gs);
 
    }
    
      public static int heuristicaDijkstra(HexGameStatus gameState) {
          PlayerType currentPlayer = gameState.getCurrentPlayer();
           PlayerType opponent = PlayerType.opposite(currentPlayer); 
        int miDistancia = Heuristica_Player2.calcularDistanciaDijkstra(gameState, currentPlayer);
        int suDistancia = Heuristica_Player2.calcularDistanciaDijkstra(gameState, opponent);
        System.out.println("Distancia para " + currentPlayer + ": " + miDistancia);
        System.out.println("Distancia para " + opponent + ": " + suDistancia);
        
        return 2 * suDistancia - miDistancia;  
        // Maximiza la diferencia a tu favor
    }
    
}

package edu.upc.epsevg.prop.hex.players;

import edu.upc.epsevg.prop.hex.*;

import java.awt.Point;
import java.util.*;

/**
 * Implementació d'un jugador automàtic per al joc HEX utilitzant l'algorisme MiniMAX
 * amb poda alfa-beta i profunditat iterativa. Aquest jugador incorpora una heurística
 * específica que bifurca segons el jugador (Player1 o Player2).
 * 
 * Aquesta classe implementa les interfícies {@link IPlayer} i {@link IAuto} i pot ser utilitzada
 * en partides automàtiques o amb control de temps.
 * 
 * @author Eric Millan and Marc Puertas
 */
public class sHEXpeare implements IPlayer, IAuto {
    private volatile boolean timeExpired = false; //Indica si el temps d'execució ha expirat.
    private PlayerMove millorMoviment = null; //Emmagatzema el millor moviment calculat.
    String name; //Nom del jugador
    private final int depth; //Profunditat màxima de l'algorisme MiniMAX.
    boolean iterative; //Indica si s'utilitza profunditat iterativa.
    private final boolean usarPoda;
 
    private int nodosExplorados; //Comptador de nodes explorats durant el càlcul del moviment.
    PlayerType currentPlayer; //Tipus de jugador actual (Player1 o Player2).
    PlayerType opponent; //Tipus de jugador contrari.
 
    /**
     * Constructor del jugador.
     * 
     * @param depth     Profunditat màxima de l'algorisme MiniMAX.
     * @param iterative Indica si s'utilitza profunditat iterativa.
     * @param flag      Indica si s'utilitza profunditat iterativa.
     */
    public sHEXpeare(int depth, boolean iterative, boolean flag) {
        this.name = "sHEXpeare";
        this.depth = depth;
        this.iterative = iterative;
        usarPoda = flag;
    }
    
    /**
     * Calcula el següent moviment del jugador utilitzant MiniMAX amb poda alfa-beta.
     * Si s'activa la profunditat iterativa, el càlcul es fa de manera incremental.
     * 
     * @param gameState L'estat actual del joc.
     * @return El moviment seleccionat.
     */
    @Override
    public PlayerMove move(HexGameStatus gameState) {
        millorMoviment = null;
        timeExpired = false;
        int profunditat = 1;
        currentPlayer = gameState.getCurrentPlayer();
        opponent = PlayerType.opposite(currentPlayer);  
        long startTime = System.currentTimeMillis();
        
        if(iterative){
            while(!timeExpired){
                //Iteramos la profundidad hasta que nos corte el TimeOut
                nodosExplorados = 0;
                Point millorMovimentActual = iterarMinimax(gameState, profunditat);
                if (!timeExpired) {
                    //Guardamos el MinMax solo si se completa con la profundidad actual
                    millorMoviment = new PlayerMove(millorMovimentActual, nodosExplorados, profunditat, SearchType.MINIMAX_IDS);
                }
                profunditat++;
            }
        }
        else{
            profunditat = depth;
            nodosExplorados = 0;
            Point millorMovimentActual = iterarMinimax(gameState, profunditat);
            long endTime = System.currentTimeMillis();
            System.out.println("Temps: " + (endTime-startTime) + " Profunditat: " + profunditat + " Nodes Explorats: " + nodosExplorados);
            return new PlayerMove(millorMovimentActual, nodosExplorados, profunditat, SearchType.MINIMAX);
        }  
        timeExpired = false;
        return millorMoviment;
    }
    
    /**
     * Itera sobre els possibles moviments utilitzant l'algorisme MiniMAX.
     * 
     * @param gameState  L'estat actual del joc.
     * @param profunditat La profunditat actual d'exploració.
     * @return El millor moviment trobat.
     */
    public Point iterarMinimax(HexGameStatus gameState, int profunditat) {
        Point millorMoviment = null;
        Point fallbackMove = gameState.getMoves().get(0).getPoint();
        int alpha = Integer.MIN_VALUE;
       
        for (MoveNode move : gameState.getMoves()) {
            if (timeExpired && iterative) break;

            HexGameStatus newState = new HexGameStatus(gameState);
            newState.placeStone(move.getPoint());
 
            if (newState.isGameOver()) return move.getPoint(); //Si se acaba el tablero significa que es jugada ganadora
            
            int valor = minValor(newState, profunditat - 1, alpha, Integer.MAX_VALUE);
            if (valor > alpha) {
                alpha = valor;
                millorMoviment = move.getPoint();
            }
        }
        return millorMoviment != null ? millorMoviment : fallbackMove;
    }

    /**
     * Calcula el valor mínim en una branca de l'algorisme MiniMAX.
     * 
     * @param gameState  L'estat del joc.
     * @param profunditat La profunditat restant.
     * @param alfa       El valor alfa de poda.
     * @param beta       El valor beta de poda.
     * @return El valor mínim calculat.
     */
    public int minValor(HexGameStatus gameState, int profunditat, int alfa, int beta) {
        nodosExplorados++;
        
        if ((timeExpired && iterative) || gameState.isGameOver() || profunditat == 0) {
            return  heuristicaDijkstra(gameState);
        }
        int valor = Integer.MAX_VALUE;
        
        for (MoveNode move : gameState.getMoves()) {
            if (timeExpired && iterative) break;
            
            HexGameStatus newState = new HexGameStatus(gameState);
            newState.placeStone(move.getPoint());

            if (newState.isGameOver()) return Integer.MIN_VALUE;
            
            valor = Math.min(valor, maxValor(newState, profunditat - 1, alfa, beta));       
            if (usarPoda && valor <= alfa) return valor;
            if (usarPoda) beta = Math.min(beta, valor);
        }
        return valor;
    }
    /**
     * Calcula el valor màxim en una branca de l'algorisme MiniMAX.
     * 
     * @param gameState  L'estat del joc.
     * @param profunditat La profunditat restant.
     * @param alfa       El valor alfa de poda.
     * @param beta       El valor beta de poda.
     * @return El valor màxim calculat.
     */
    public int maxValor(HexGameStatus gameState, int profunditat, int alfa, int beta) {
        nodosExplorados++;

        if ((timeExpired && iterative) || gameState.isGameOver() || profunditat == 0) {
            return heuristicaDijkstra(gameState);
        }
        int valor = Integer.MIN_VALUE;

        for (MoveNode move : gameState.getMoves()) {
            if (timeExpired && iterative) break;

            HexGameStatus newState = new HexGameStatus(gameState);            
            newState.placeStone(move.getPoint());

            if (newState.isGameOver()) return Integer.MAX_VALUE;      

            valor = Math.max(valor, minValor(newState, profunditat - 1, alfa, beta));
            if (usarPoda && valor >= beta) return valor;
            if (usarPoda) alfa = Math.max(alfa, valor);
        }
        return valor;
    }

    /**
     * Finalitza l'execució quan el temps d'execució expira.
     */   
    @Override
    public void timeout() {
        timeExpired = true;
    }

    /**
     * Retorna el nom del jugador.
     * 
     * @return El nom del jugador.
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Calcula la heurística basada en l'algorisme de Dijkstra, depenent del jugador.
     * 
     * @param gameState L'estat actual del joc.
     * @return El valor heurístic calculat.
     */
    public int heuristicaDijkstra(HexGameStatus gameState) {
        if (currentPlayer == PlayerType.PLAYER1) {
            return heuristicaPlayer1(gameState);
        } else {
            return heuristicaPlayer2(gameState);
        }
    }

    /**
     * Heurística específica per al jugador 1.
     * 
     * @param gameState L'estat actual del joc.
     * @return El valor heurístic calculat.
     */
    public int heuristicaPlayer1(HexGameStatus gameState) {
        int miDistancia = Heuristica_Player1.calcularDistanciaDijkstra(gameState, currentPlayer);
        int suDistancia = Heuristica_Player1.calcularDistanciaDijkstra(gameState, opponent);
        return 2 * suDistancia - miDistancia; 
    }

    /**
     * Heurística específica per al jugador 2.
     * 
     * @param gameState L'estat actual del joc.
     * @return El valor heurístic calculat.
     */
    public int heuristicaPlayer2(HexGameStatus gameState) {
        int miDistancia = Heuristica_Player2.calcularDistanciaDijkstra(gameState, currentPlayer);
        int suDistancia = Heuristica_Player2.calcularDistanciaDijkstra(gameState, opponent);
        return 2 * suDistancia - miDistancia; 

    }

}
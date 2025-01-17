package edu.epsevg.prop.lab.c4;

import java.util.ArrayList;

/**
 * Classe Fumate4.
 * Aquesta classe utilitza l'algoritme Minimax amb poda alfa-beta per determinar els moviments òptims.
 *
 * @author Andreu Pino Alcalà
 * @author Marc Puertas Riba
 */
public class Fumate4 implements Jugador, IAuto {

    //Nom del jugador.
    private final String nom;
    
    //Color del jugador (1 o -1).
    private int color;
    
    
    //Profunditat màxima que explora l'algoritme Minimax.
    private final int profunditat;
    
    //Direccions per explorar l'heurística.
    private final ArrayList<int[]> direccions;
    
    //Indica si s'utilitza la poda alfa-beta.
    private final boolean alfabeta;
    
    
    //Format de sortida de dades en CSV per a diagnòstic.
    //Si true, produeix sortida en format CSV. Si false, mostra informació detallada.
    private final boolean csvOutput;
    
    
    //Comptador del número de jugades realitzades.
    private int numJugada;
    
    
    //Nombre de vegades que es crida a l'heurística durant una jugada.
    private int numHeur;
    
    //Temps en nanosegons emprat per calcular el moviment actual.
    public long timeTurn;

    /**
     * Constructor del jugador Fumate4.
     *
     * @param profunditat profunditat màxima de l'algoritme Minimax.
     * @param alfabeta indica si cal utilitzar la poda alfa-beta.
     */
    public Fumate4(int profunditat, boolean alfabeta) {
        this.nom = "Fumate4";
        this.profunditat = profunditat;
        this.direccions = new ArrayList<>();
        this.alfabeta = alfabeta;
        this.csvOutput = false;
        this.numHeur = 0;
        this.numJugada = 0;
        
        if (this.csvOutput) {
            System.out.println("tirada;numheur");
        }

        direccions.add(new int[] { 1, 0 });
        direccions.add(new int[] { 1, 1 });
        direccions.add(new int[] { 0, 1 });
        direccions.add(new int[] { -1, 1 });
        direccions.add(new int[] { -1, 0 });
    }

    /**
     * Determina el moviment òptim a fer en el tauler actual.
     *
     * @param t el tauler actual.
     * @param color el color del jugador.
     * @return la columna on es farà el moviment.
     */
    @Override
    public int moviment(Tauler t, int color) {
        this.timeTurn = System.nanoTime();
        this.color = color;
        int depth = profunditat;
        this.numJugada++;
        int eleccio = obtenerCol(t, depth, alfabeta);
        this.timeTurn = System.nanoTime() - this.timeTurn;

        if (!this.csvOutput) {
            System.out.println("Temps en calcular moviment: " + (this.timeTurn / 1000000) + "ms\n");
        }

        return eleccio;
    }

    /**
     * Troba la millor columna per moure utilitzant l'algoritme Minimax amb o sense poda alfa-beta.
     *
     * @param t el tauler actual.
     * @param auxProfunditat profunditat restant a explorar.
     * @param alfabeta indica si cal utilitzar poda alfa-beta.
     * @return la millor columna per al moviment.
     */
    public int obtenerCol(Tauler t, int auxProfunditat, boolean alfabeta) {
        this.numHeur = 0;
        int bestHeur = Integer.MIN_VALUE;
        int bestJugada = -1;

        for (int i = 0; i < t.getMida(); i++) {
            int heur = Integer.MIN_VALUE;

            if (t.movpossible(i)) {
                Tauler aux = new Tauler(t);
                aux.afegeix(i, this.color);

                if (aux.solucio(i, this.color)) {
                    return i; 
                } else {
                    heur = alfabeta ? 
                           minimaxGeneral(aux, auxProfunditat - 1, false, true, bestHeur, Integer.MAX_VALUE) :
                           minimaxGeneral(aux, auxProfunditat - 1, false, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    if (heur > bestHeur || bestJugada == -1) {
                        bestJugada = i;
                        bestHeur = heur;
                    }
                }
            }

            if (!this.csvOutput) {
                System.out.println("Columna " + i + " heur: " + heur);
            }
        }

        if (!this.csvOutput) {
            System.out.println("Jugada escollida: " + bestJugada + "\n" + "Nombre de jugades finals explorades: " + this.numHeur);
        }

        return bestJugada;
    }
    
    /**
     * Mètode que realitza el desenvolupament de l'arbre heurístic MiniMax de manera recursiva.
     * Comprova el valor màxim si {@code isMax} és cert i, en cas contrari, mira el valor mínim.
     * Això ho fa utilitzant la poda alfa-beta per evitar explorar branques innecessàries de l'arbre.
     *
     * @param t el tauler actual.
     * @param profunditat la profunditat màxima a explorar.
     * @param isMax indica si és el torn del jugador màxim.
     * @param usarAlfaBeta si és {@code true}, s'usa la poda alfa-beta.
     * @param alfa el valor mínim de la poda alfa-beta.
     * @param beta el valor màxim de la poda alfa-beta.
     * @return el millor valor heurístic per a la jugada.
     */
    public int minimaxGeneral(Tauler t, int profunditat, boolean isMax, boolean usarAlfaBeta, int alfa, int beta) {
        
        if (profunditat <= 0) 
            return heuristica(t);
        
        if (isMax) {
            int bestValor = Integer.MIN_VALUE;
            for (int i = 0; i < t.getMida(); i++) {
                if (t.movpossible(i)) {
                    Tauler aux = new Tauler(t);
                    aux.afegeix(i, this.color);
                    
                    if (aux.solucio(i, this.color)) 
                        return Integer.MAX_VALUE; 
                    
                    else {
                        int valor = minimaxGeneral(aux, profunditat - 1, false, usarAlfaBeta, alfa, beta);
                        bestValor = Math.max(bestValor, valor);
                        if (usarAlfaBeta) {
                            alfa = Math.max(alfa, bestValor);
                            if (alfa >= beta) 
                                break;
                        }
                    }
                }
            }
            return bestValor;
            
        } 
        
        else {
            int bestValor = Integer.MAX_VALUE;
            for (int i = 0; i < t.getMida(); i++) {
                if (t.movpossible(i)) {
                    Tauler aux = new Tauler(t);
                    aux.afegeix(i, this.color * -1);
                    
                    if (aux.solucio(i, this.color * -1)) 
                        return Integer.MIN_VALUE;
                    
                    else {
                        int valor = minimaxGeneral(aux, profunditat - 1, true, usarAlfaBeta, alfa, beta);
                        bestValor = Math.min(bestValor, valor);
                        if (usarAlfaBeta) {
                            beta = Math.min(beta, bestValor);
                            if (alfa >= beta) 
                                break; 
                        }
                    }
                }
            }
            return bestValor;
        }
    }

 
    /**
     * Mètode que calcula el valor heurístic d'un tauler.
     * El càlcul prioritza les fitxes del jugador que estan juntes i descompta punts per les fitxes del rival.
     *
     * @param t el tauler actual de la partida.
     * @return la diferència entre els punts del jugador i els punts del rival.
     */
    public int heuristica(Tauler t) {
        
        this.numHeur++;

        int puntsJo = 0;
        int puntsEnemic = 0;
        int size = t.getMida();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (t.getColor(i, j) == this.color) {
                    for (int[] direc : direccions) {
                        int dirX = direc[0];
                        int dirY = direc[1];
                        int punts = valorCasella(t, i, j, dirX, dirY, this.color);
                        puntsJo += punts;
                    }
                }

                else if (t.getColor(i, j) == this.color*-1) {
                    for (int[] direc : direccions) {
                        int dirX = direc[0];
                        int dirY = direc[1];
                        int punts = valorCasella(t, i, j, dirX, dirY, this.color*-1);
                        puntsEnemic += punts;
                    }
                }
            }
        }
        return puntsJo - puntsEnemic;
    }
         
    /**
     * Aquest mètode retorna el valor heurístic d'una casella en una direcció específica.
     * Es considera fins a 4 posicions consecutives en una direcció determinada, sempre que no es trobin límits del tauler.
     * 
     * @param t el tauler actual.
     * @param i la fila inicial.
     * @param j la columna inicial.
     * @param direccioX la direcció en l'eix X.
     * @param direccioY la direcció en l'eix Y.
     * @param color el color del jugador (1 per al jugador actual, -1 per al rival).
     * @return el valor heurístic d'aquesta direcció per al jugador especificat.
     */
    public int valorCasella (Tauler t, int i, int j, int direccioX, int direccioY, int color) {
        
        int size = t.getMida();
        int score = 0;
        
        for (int k = 0; k < 4; k++) {
            i += direccioX;
            j += direccioY;
            
            if (i < 0 || i >= size || j < 0 || j >= size) 
                break;
            
            int colorPos = t.getColor(i, j);
            if (colorPos == 0) 
                score += 3;
            
            else {
                if (colorPos == color) 
                    score += 2;                    
                
                else score -= 1;     
            }
        }
        return score;
    }

    /**
     * Retorna el nom del jugador.
     *
     * @return el nom del jugador, que en aquest cas és {@code "Fumate4"}.
     */
    @Override
    public String nom() {
        return nom;
    }
}
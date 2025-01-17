package edu.upc.epsevg.prop.hex;

import edu.upc.epsevg.prop.hex.players.HumanPlayer;
import edu.upc.epsevg.prop.hex.players.RandomPlayer;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.IPlayer;
import edu.upc.epsevg.prop.hex.players.H_E_X_Player;
import edu.upc.epsevg.prop.hex.players.sHEXpeare;



import javax.swing.SwingUtilities;

/**
 * Checkers: el joc de taula.
 * @author bernat
 */
public class Game {
        /**
     * @param args
     */
    public static void main(String[] args) { 
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                IPlayer player1 = new HumanPlayer("manuel"/*GB*/);
                
                IPlayer player2 = new sHEXpeare(5, true, true);
                
                IPlayer player3 = new H_E_X_Player(2);
                
                IPlayer player4 = new RandomPlayer("Random");
                
                
                new Board(player3 , player2, 11 /*mida*/,  10/*s*/, false);
             }
        });
    }
}

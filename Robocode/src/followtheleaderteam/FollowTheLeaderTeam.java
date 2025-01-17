/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package followtheleaderteam;
import robocode.TeamRobot;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import java.awt.Graphics2D;
import robocode.RobotDeathEvent;

/**
 * Classe principal que implementa el comportament del robot que segueix el líder.
 */
public class FollowTheLeaderTeam extends TeamRobot {
    private RobotState1 currentState;

    /**
     * Inicia l'execució del robot, configurant l'estat inicial i ajustant el radar i el canó.
     */
    @Override
    public void run() {
        // Inicialitza l'estat actual a Fase0State
        currentState = new Fase0State();
        setAdjustRadarForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
        
        while (true) {
            // Executa l'estat actual del robot
            currentState.executeState(this);
            // Executa el cicle principal del robot
            execute();
        }
    }

    /**
     * Gestor d'esdeveniments que s'executa quan es rep un missatge.
     *
     * @param event L'esdeveniment de missatge rebut.
     */
    @Override
    public void onMessageReceived(MessageEvent event) {
        // Tracta el missatge rebut en l'estat actual
        currentState.onMessageReceived(this, event);
        
        // Comprova si el missatge és una cadena
        if (event.getMessage() instanceof String) {
            String message = (String) event.getMessage();
            // Si el missatge indica que la fase 0 ha acabat, canvia a la fase 1
            if (message.equals("Fase0Complete")) {
                currentState = new Fase1State();
            }
        }
    }

    /**
     * Gestor d'esdeveniments que s'executa quan es detecta un altre robot.
     *
     * @param event L'esdeveniment de robot detectat.
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        currentState.onScannedRobot(this, event);
    }

    /**
     * Dibuixa informació a la pantalla en l'estat actual.
     *
     * @param g L'objecte Graphics2D utilitzat per dibuixar.
     */
    @Override
    public void onPaint(Graphics2D g) {
        currentState.onPaint(this, g);
    }
    
    /**
     * Gestor d'esdeveniments que s'executa quan un robot mor.
     *
     * @param event L'esdeveniment de mort del robot.
     */
    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        currentState.onRobotDeath(this, event);
    }
}

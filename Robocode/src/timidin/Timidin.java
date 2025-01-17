package timidin;

import robocode.*;
import java.awt.geom.Point2D;

/**
 * Classe principal del robot Timidin que estèn la classe AdvancedRobot.
 * Aquest robot canvia el seu comportament dinàmicament segons l'estat actual en què es trobi.
 */
public class Timidin extends AdvancedRobot {
    private RobotState currentState;
    private double targetX;
    private double targetY;

    /**
     * Mètode principal que s'executa en bucle mentre el robot està actiu.
     * Comença en l'estat Phase0State i després es va actualitzant segons el comportament definit en els estats.
     */
    @Override
    public void run() {
        currentState = new Phase0State(); // Comença amb Phase0
        while (true) {
            currentState.execute(this); 
            execute(); 
        }
    }

    /**
     * Canvia l'estat actual del robot a un de nou.
     * @param newState El nou estat al qual ha de canviar el robot.
     */
    public void changeState(RobotState newState) {
        currentState = newState;
    }

    /**
     * Calcula la cantonada més llunyana del camp de batalla en relació a la posició d'un robot enemic.
     * Després, transiciona a l'estat Phase1State.
     * @param enemyX Coordenada X del robot enemic.
     * @param enemyY Coordenada Y del robot enemic.
     */
    public void calculateFurthestCorner(double enemyX, double enemyY) {
        // Càlcul de la cantonada més llunyana
        // ...
        // Transició a l'estat Phase1State
        Phase1State phase1 = new Phase1State(targetX, targetY);
        changeState(phase1);
    }

    /**
     * Estableix el moviment del robot cap endavant o cap enrere.
     * @param distance Distància que ha de recórrer el robot.
     */
    public void setRobotMovement(double distance) {
        setAhead(distance);
    }

    /**
     * Gira el robot un angle específic.
     * @param angle Angle en graus per girar el robot.
     */
    public void turnRobot(double angle) {
        setTurnRight(angle);
    }

    // Mètodes per gestionar esdeveniments de Robocode, delegant a l'estat actual
    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        currentState.onScannedRobot(this, event);
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        currentState.onHitWall(this, event);
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        currentState.onHitRobot(this, event);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        currentState.onHitByBullet(this, event);
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        currentState.onRobotDeath(this, event);
    }
}

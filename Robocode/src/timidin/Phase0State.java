package timidin;

import robocode.*;
/**
 * Fase inicial del comportament del robot Timidin.
 * L'objectiu d'aquesta fase és detectar el robot enemic mitjançant la rotació del radar.
 */
public class Phase0State implements RobotState {
    private boolean enemyDetected = false;

    /**
     * Executa la lògica d'aquesta fase. Si no s'ha detectat cap enemic, el radar continua girant.
     * @param robot La instància del robot que executa aquest estat.
     */
    @Override
    public void execute(Timidin robot) {
        if (!enemyDetected) {
            robot.setTurnRadarRight(360); // Continuar girant el radar
        }
    }

    /**
     * Gestiona l'esdeveniment de detecció d'un robot enemic.
     * Atura el radar i calcula la cantonada més llunyana del camp de batalla per moure's cap a ella.
     * @param robot La instància del robot que executa aquest estat.
     * @param event Esdeveniment que conté informació sobre el robot detectat.
     */
    @Override
    public void onScannedRobot(Timidin robot, ScannedRobotEvent event) {
        if (!enemyDetected) {
            enemyDetected = true;
            double enemyBearing = robot.getHeading() + event.getBearing();
            double enemyDistance = event.getDistance();
            double enemyX = robot.getX() + enemyDistance * Math.sin(Math.toRadians(enemyBearing));
            double enemyY = robot.getY() + enemyDistance * Math.cos(Math.toRadians(enemyBearing));
            robot.calculateFurthestCorner(enemyX, enemyY); // Calcular la cantonada més llunyana
            robot.setTurnRadarRight(0); // Aturar el radar
        }
    }

    /**
     * Gestiona el que passa quan el robot xoca amb una paret.
     * @param robot La instància del robot que executa aquest estat.
     * @param event Esdeveniment que conté informació sobre la col·lisió amb la paret.
     */
    @Override
    public void onHitWall(Timidin robot, HitWallEvent event) {
        robot.setBack(50);
        robot.setTurnRight(90);
    }

    /**
     * Gestiona el que passa quan el robot xoca contra un altre robot.
     * @param robot La instància del robot que executa aquest estat.
     * @param event Esdeveniment que conté informació sobre la col·lisió amb un altre robot.
     */
    @Override
    public void onHitRobot(Timidin robot, HitRobotEvent event) {
        robot.setBack(50);
        robot.setTurnRight(90);
    }

    /**
     * Gestiona el que passa quan el robot és colpejat per una bala.
     * @param robot La instància del robot que executa aquest estat.
     * @param event Esdeveniment que conté informació sobre l'impacte de la bala.
     */
    @Override
    public void onHitByBullet(Timidin robot, HitByBulletEvent event) {
        robot.setBack(50);
        robot.setTurnRight(90);
    }

    /**
     * Gestiona el que passa quan un robot enemic mor.
     * @param robot La instància del robot que executa aquest estat.
     * @param event Esdeveniment que conté informació sobre la mort d'un robot enemic.
     */
    @Override
    public void onRobotDeath(Timidin robot, RobotDeathEvent event) {
    }
}

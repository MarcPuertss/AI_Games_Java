package timidin;

import robocode.*;

/**
 * Fase 1 del comportament del robot Timidin.
 * En aquesta fase, el robot es mou cap a la cantonada més allunyada calculada a la Fase 0.
 * Si detecta obstacles com altres robots o parets, realitza maniobres d'esquiva.
 */
public class Phase1State implements RobotState {
    private double targetX;
    private double targetY;
    private static final double ARRIVAL_THRESHOLD = 50; // Umbral per considerar que ha arribat
    private boolean evading = false; // Indica si està esquivant
    private int evadeStep = 0; // Pas actual de la maniobra d'esquiva

    /**
     * Constructor de la Fase 1.
     * @param x Coordenada X de la cantonada objectiu.
     * @param y Coordenada Y de la cantonada objectiu.
     */
    public Phase1State(double x, double y) {
        this.targetX = x;
        this.targetY = y;
    }

    /**
     * Executa la lògica de la Fase 1.
     * Si no s'està esquivant, el robot es mou cap a l'objectiu.
     * Si s'està esquivant, es realitzen maniobres d'esquiva.
     * @param robot La instància del robot Timidin.
     */
    @Override
    public void execute(Timidin robot) {
        if (!evading) {
            moveTowardsTarget(robot);
        } else {
            evadeEnemy(robot); // Si està esquivant, no avança cap a l'objectiu
        }
    }

    /**
     * Mou el robot en direcció a l'objectiu.
     * Quan arriba a la cantonada, canvia a la Fase 2.
     * @param robot La instància del robot Timidin.
     */
    private void moveTowardsTarget(Timidin robot) {
        double dx = targetX - robot.getX();
        double dy = targetY - robot.getY();
        double distance = Math.hypot(dx, dy);

        if (distance < ARRIVAL_THRESHOLD) {
            robot.changeState(new Phase2State()); // Canvia a la Fase 2 en arribar
            return;
        }

        double angleToTarget = Math.toDegrees(Math.atan2(dx, dy)) - robot.getHeading();
        angleToTarget = normalizeBearing(angleToTarget);
        
        if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) { // Només gira si està aturat
            robot.setTurnRight(angleToTarget); // Girar cap a l'objectiu
        }

        if (Math.abs(angleToTarget) < 10) { // Si l'angle és petit, avança
            robot.setAhead(distance * 0.8); // Avança cap a l'objectiu amb marge
        }
    }

    /**
     * Realitza una maniobra d'esquiva en diferents passos per evitar un enemic o obstacle.
     * @param robot La instància del robot Timidin.
     */
    private void evadeEnemy(Timidin robot) {
        switch (evadeStep) {
            case 0: // Primer gir de 90 graus
                robot.fire(10);
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnRight(90);
                    evadeStep++;
                }
                break;
            case 1: // Avançar una mica
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setAhead(100); // Ajusta la distància segons calgui
                    evadeStep++;
                }
                break;
            case 2: // Segon gir de 90 graus
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnLeft(90);
                    evadeStep++;
                }
                break;
            case 3: // Avançar per superar l'enemic
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setAhead(100);
                    evadeStep++;
                }
                break;
            case 4: // Tercer gir per reprendre la ruta
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnLeft(90);
                    evadeStep++;
                }
                break;
            case 5: // Avançar una mica més
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setAhead(100);
                    evadeStep++;
                }
                break;
            case 6: // Últim gir per tornar a la ruta original
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnRight(90);
                    evadeStep = 0;
                    evading = false; // Finalitza l'esquiva
                }
                break;
        }
    }

    /**
     * Gestiona la detecció d'un robot enemic.
     * Si l'enemic està a prop, inicia una maniobra d'esquiva.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de detecció d'un robot enemic.
     */
    @Override
    public void onScannedRobot(Timidin robot, ScannedRobotEvent event) {
        double enemyDistance = event.getDistance();

        if (enemyDistance < 150 && !evading) { 
           // Si l'enemic està a prop, començar a esquivar
            evading = true;
            evadeStep = 0;
        }
    }

    /**
     * Gestiona el que passa quan el robot xoca amb una paret.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de col·lisió amb una paret.
     */
    @Override
    public void onHitWall(Timidin robot, HitWallEvent event) {
        robot.setBack(50);
        robot.setTurnRight(90);
    }

    /**
     * Gestiona el que passa quan el robot xoca amb un altre robot.
     * Si no s'està esquivant, comença una maniobra d'esquiva.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de col·lisió amb un altre robot.
     */
    @Override
    public void onHitRobot(Timidin robot, HitRobotEvent event) {
        if (!evading) { // Iniciar maniobra d'esquiva si xoca
            evading = true;
            evadeStep = 0;
        }
    }

    /**
     * Opcional: Gestiona el que passa quan el robot és colpejat per una bala.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de l'impacte d'una bala.
     */
    @Override
    public void onHitByBullet(Timidin robot, HitByBulletEvent event) {
        // Opcional: Manejar ser colpejat per una bala
    }

    /**
     * Gestiona el que passa quan un robot enemic mor.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de mort d'un robot enemic.
     */
    public void onRobotDeath(Timidin robot, RobotDeathEvent event) {
        // Accions quan mor un robot enemic
    }

    /**
     * Normalitza l'angle per mantenir-lo entre -180 i 180 graus.
     * @param angle L'angle a normalitzar.
     * @return L'angle normalitzat.
     */
    private double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}

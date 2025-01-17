package timidin;
import robocode.*;

/**
 * Fase 2 del comportament del robot Timidin.
 * En aquesta fase final, el robot es posiciona a la cantonada i comença una acció defensiva.
 * Gira el radar contínuament per escanejar el camp de batalla i, quan detecta un enemic,
 * ajusta el canó per apuntar amb precisió i dispara.
 */
public class Phase2State implements RobotState {

    private boolean enemy_found = false; // Indica si s'ha trobat un enemic
    private String enemy_name = ""; // Variable per emmagatzemar el nom de l'enemic
    private double firepower; // Potència de dispar

    /**
     * Executa la lògica de la Fase 2.
     * El robot es manté quiet i gira el radar per buscar enemics. Si es detecta un enemic,
     * es dispara amb una potència ajustada segons la distància.
     * @param robot La instància del robot Timidin.
     */
    @Override
    public void execute(Timidin robot) {
        // Aturar completament
        robot.setAhead(0);
        robot.setTurnRight(0);

        // Girar el radar contínuament per buscar enemics
        if (!enemy_found) {
            robot.setTurnRadarRight(360); // Rotar el radar per escanejar el camp
        } else {
            robot.fire(firepower); // Disparar si s'ha trobat un enemic
        }
    }

    /**
     * Gestiona la detecció d'un robot enemic.
     * Quan es detecta un enemic, ajusta el canó per apuntar amb precisió i calcula la potència de dispar segons la distància.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de detecció d'un robot enemic.
     */
    @Override
    public void onScannedRobot(Timidin robot, ScannedRobotEvent event) {
        // Girar el canó cap a l'enemic
        double absoluteBearing = robot.getHeading() + event.getBearing();
        double gunTurn = absoluteBearing - robot.getGunHeading();
        robot.setTurnGunRight(normalizeBearing(gunTurn));

        enemy_found = true;
        enemy_name = event.getName(); // Emmagatzemar el nom de l'enemic
        firepower = Math.min(3.0, Math.max(1.0, 400 / event.getDistance())); // Ajustar la potència segons la distància
    }

    /**
     * Gestiona la mort d'un robot enemic.
     * Si el robot mort és l'enemic que es rastrejava, reinicia la cerca.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de mort d'un robot enemic.
     */
    public void onRobotDeath(Timidin robot, RobotDeathEvent event) {
        if (event.getName().equals(enemy_name)) {
            enemy_found = false; // Reiniciar la cerca si era l'enemic rastrejat
        }
    }

    /**
     * Gestiona el que passa quan el robot xoca amb una paret.
     * En aquest cas, retrocedeix i gira per evitar quedar atrapat.
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
     * Dispara immediatament i realitza maniobres per evitar quedar atrapat.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de col·lisió amb un altre robot.
     */
    @Override
    public void onHitRobot(Timidin robot, HitRobotEvent event) {
        robot.fire(firepower);
        robot.setBack(50);
        robot.setTurnRight(90);
    }

    /**
     * Opcional: Gestiona el que passa quan el robot és colpejat per una bala.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de l'impacte d'una bala.
     */
    @Override
    public void onHitByBullet(Timidin robot, HitByBulletEvent event) {
        // Lògica opcional si és colpejat per una bala
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

package timidin;
import robocode.*;

/**
 * Interfície que defineix l'estat d'un robot en el context del comportament del Timidin.
 * Cada estat implementarà les accions que el robot haurà de realitzar i com respondre
 * a diversos esdeveniments durant la batalla.
 */
public interface RobotState {

    /**
     * Executa la lògica associada a l'estat actual del robot.
     * @param robot La instància del robot Timidin.
     */
    void execute(Timidin robot);

    /**
     * Gestiona l'esdeveniment quan es detecta un altre robot.
     * Permet definir com reaccionar quan el radar escaneja un enemic.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de detecció d'un robot enemic.
     */
    void onScannedRobot(Timidin robot, ScannedRobotEvent event);

    /**
     * Gestiona l'esdeveniment quan el robot xoca amb una paret.
     * Defineix com ha de respondre el robot en aquesta situació.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de col·lisió amb una paret.
     */
    void onHitWall(Timidin robot, HitWallEvent event);

    /**
     * Gestiona l'esdeveniment quan el robot xoca amb un altre robot.
     * Defineix les accions a realitzar en cas de col·lisió amb un altre tanc.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de col·lisió amb un altre robot.
     */
    void onHitRobot(Timidin robot, HitRobotEvent event);

    /**
     * Gestiona l'esdeveniment quan el robot és colpejat per una bala.
     * Defineix com ha de reaccionar el robot quan és atacat.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de l'impacte d'una bala enemiga.
     */
    void onHitByBullet(Timidin robot, HitByBulletEvent event);

    /**
     * Gestiona l'esdeveniment quan un altre robot mor.
     * Defineix la resposta del robot quan es produeix la mort d'un enemic en batalla.
     * @param robot La instància del robot Timidin.
     * @param event Esdeveniment de mort d'un altre robot.
     */
    void onRobotDeath(Timidin robot, RobotDeathEvent event);
}

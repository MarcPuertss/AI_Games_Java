/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package followtheleaderteam;

import robocode.TeamRobot;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import java.awt.Graphics2D;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;

/**
 * Aquesta interfície defineix els mètodes que els estats dels robots del equip han d'implementar.
 */
public interface RobotState1 {
    /**
     * Executa l'estat actual del robot.
     * 
     * @param robot El robot que executa aquest estat.
     */
    void executeState(TeamRobot robot);
    
    /**
     * Gestor d'esdeveniments per quan es rep un missatge.
     * 
     * @param robot El robot que rep el missatge.
     * @param event L'esdeveniment del missatge rebut.
     */
    void onMessageReceived(TeamRobot robot, MessageEvent event);
    
    /**
     * Gestor d'esdeveniments per quan es detecta un altre robot.
     * 
     * @param robot El robot que detecta un altre robot.
     * @param event L'esdeveniment del robot detectat.
     */
    void onScannedRobot(TeamRobot robot, ScannedRobotEvent event);
    
    /**
     * Dibuixa informació a la pantalla.
     * 
     * @param robot El robot que dibuixa.
     * @param g L'objecte Graphics2D utilitzat per dibuixar.
     */
    void onPaint(TeamRobot robot, Graphics2D g);
    
    /**
     * Gestor d'esdeveniments per quan el robot colpeja una paret.
     * 
     * @param robot El robot que colpeja la paret.
     * @param event L'esdeveniment de la col·lisió amb la paret.
     */
    void OnHitWall(TeamRobot robot, HitWallEvent event);
    
    /**
     * Gestor d'esdeveniments per quan un robot mor.
     * 
     * @param robot El robot que mor.
     * @param event L'esdeveniment de la mort del robot.
     */
    void onRobotDeath(TeamRobot robot, RobotDeathEvent event);
}

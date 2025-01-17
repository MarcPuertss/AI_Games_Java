package followtheleaderteam;

import robocode.TeamRobot;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;

/**
 * Classe que implementa la fase inicial de selecció de líder basada en la posició X.
 */
public class Fase0State implements RobotState1 {

    private final Map<String, Double> positionsX = new HashMap<>();
    private boolean leaderSelected = false;
    private String leaderName = "";
    private final int TEAM_SIZE = 5; 

    /**
     * Executa l'estat actual si no s'ha seleccionat un líder.
     *
     * @param robot El robot que executa l'estat.
     */
    @Override
    public void executeState(TeamRobot robot) {
        if (!leaderSelected) {
            double initialX = robot.getX();
            positionsX.put(robot.getName(), initialX);
            try {
                // Envia la posició inicial X del robot al grup
                String message = "Position:" + robot.getName() + ":" + initialX;
                robot.broadcastMessage(message);
                robot.out.println(robot.getName() + " ha enviat la seva posició X inicial: " + initialX);
            } catch (IOException e) {
                // Gestió d'errors si és necessari
            }
        }
    }

    /**
     * Gestor d'esdeveniments que s'executa quan es rep un missatge.
     *
     * @param robot El robot que rep el missatge.
     * @param event L'esdeveniment de missatge rebut.
     */
    @Override
    public void onMessageReceived(TeamRobot robot, MessageEvent event) {
        String msg = (String) event.getMessage();
        
        // Comprova si el missatge conté la posició d'un altre robot
        if (msg.startsWith("Position:")) {
            String[] parts = msg.split(":");
            if (parts.length == 3) {
                String robotName = parts[1];
                double positionX = Double.parseDouble(parts[2]);
                positionsX.put(robotName, positionX);
                robot.out.println(robot.getName() + " ha rebut la posició X de " + robotName + ": " + positionX);
                
                // Si s'han rebut les posicions de tots els robots, selecciona un líder
                if (positionsX.size() == TEAM_SIZE && !leaderSelected) {
                    selectLeader(robot);
                }
            }
        } else if (msg.startsWith("Leader:")) {
            // Actualitza el nom del líder rebut
            leaderName = msg.substring(7);
            leaderSelected = true;
            robot.out.println("Líder rebut: " + leaderName);
        }
    }

    /**
     * Gestor d'esdeveniments que s'executa quan es detecta un robot.
     *
     * @param robot El robot que executa l'esdeveniment.
     * @param event L'esdeveniment de robot detectat.
     */
    @Override
    public void onScannedRobot(TeamRobot robot, ScannedRobotEvent event) {
        // No s'ha implementat cap comportament per quan es detecta un robot.
    }

    /**
     * Dibuixa el nom del líder si s'ha seleccionat.
     *
     * @param robot El robot que executa l'estat.
     * @param g L'objecte Graphics2D utilitzat per dibuixar.
     */
    @Override
    public void onPaint(TeamRobot robot, Graphics2D g) {
        if (leaderSelected) {
            g.drawString("Líder: " + leaderName, (int) robot.getX(), (int) robot.getY());
        }
    }

    /**
     * Selecciona el robot amb la posició X més gran com a líder.
     *
     * @param robot El robot que selecciona el líder.
     */
    private void selectLeader(TeamRobot robot) {
        double maxX = -1;
        String selectedLeader = "";
        for (Map.Entry<String, Double> entry : positionsX.entrySet()) {
            if (entry.getValue() > maxX) {
                maxX = entry.getValue();
                selectedLeader = entry.getKey();
            }
        }
        leaderName = selectedLeader;
        leaderSelected = true;
        robot.out.println("El líder seleccionat és: " + leaderName);
        
        // Actualitza la jerarquia compartida
        updateHierarchy(robot);
        
        // Notifica al grup qui és el líder
        try {
            robot.broadcastMessage(leaderName);
        } catch (IOException e) {
            // Gestió d'errors si és necessari
        }
    }

    /**
     * Actualitza la jerarquia dels robots en base a la posició X.
     *
     * @param robot El robot que actualitza la jerarquia.
     */
    private void updateHierarchy(TeamRobot robot) {
        List<String> newHierarchy = new ArrayList<>();
        newHierarchy.add(leaderName);
        
        List<String> otherRobots = positionsX.keySet().stream()
            .filter(name -> !name.equals(leaderName))
            .collect(Collectors.toList());
        
        otherRobots.sort((robot1, robot2) -> {
            double distance1 = Math.abs(positionsX.get(robot1) - positionsX.get(leaderName));
            double distance2 = Math.abs(positionsX.get(robot2) - positionsX.get(leaderName));
            return Double.compare(distance1, distance2);
        });

        newHierarchy.addAll(otherRobots);
        RobotHierarchy.getInstance().setHierarchy(newHierarchy);
        robot.out.println("Jerarquia actual: " + RobotHierarchy.getInstance().getHierarchy());
        
        // Notifica que la fase 0 ha acabat
        try {
            robot.broadcastMessage("Fase0Complete");
        } catch (IOException e) {
            // Gestió d'errors si és necessari
        }
    }

    /**
     * Comportament quan el robot colpeja una paret.
     *
     * @param robot El robot que colpeja la paret.
     * @param event L'esdeveniment de col·lisió amb la paret.
     */
    @Override
    public void OnHitWall(TeamRobot robot, HitWallEvent event) {
        robot.setBack(50);
        robot.setTurnRight(90);
    }

    /**
     * Gestor d'esdeveniments que s'executa quan un robot mor.
     *
     * @param robot El robot que mor.
     * @param event L'esdeveniment de mort del robot.
     */
    @Override
    public void onRobotDeath(TeamRobot robot, RobotDeathEvent event) {
        // No s'ha implementat cap comportament per quan un robot mor.
    }
}

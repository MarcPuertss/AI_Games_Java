package followtheleaderteam;
import java.awt.Color;
import robocode.TeamRobot;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;


 /**
  * Classe que implementa la fase 1 del robot FollowLeaderTeam
  * 
  * 
  */
public class Fase1State implements RobotState1{

    private Timer timer;
    private boolean sentido_horario = true;
    private static final double CORNER_TOLERANCE = 100; 
    private Point2D.Double target; 
    private boolean evading = false;
    private int evadeStep = 0; 
    private boolean firstMove = true;  
    private boolean lastRobotAtCorner = false;
    private final RobotHierarchy hierarchy = RobotHierarchy.getInstance();
    private int turn = 0;

     /**
     * Executa l'estat del robot.
     * @param robot El robot que executa l'estat.
     */
    @Override
    public void executeState (TeamRobot robot){
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    flip_conga(robot);
                }
            }, 15000, 15000); 
        }
          
        

        if (isLeader(robot)){

            robot.setTurnRadarRight(360);
        }

        if (!evading){
            if (turn%2 == 0) broadcastPosition(robot);
            ++turn;
            if (isLeader(robot)){
                moveTL(robot);
            }
            else {
                moveFollower(robot);
                
            }

            firstMove = false;

        } else evadeTank(robot);


        if (lastRobotAtCorner) {
            robot.setTurnRadarRight(360);  
        }

    }

    /**
     * Inverteix l'ordre de la jerarquia de robots.
     * @param robot El robot que executa l'estat.
     */
    private void flip_conga(TeamRobot robot) {
     
     
     if (sentido_horario) hierarchy.substractcornerIndex(2);
     else hierarchy.sumcornerIndex(2);
     
     sentido_horario = !sentido_horario;
     
     hierarchy.reverseHierarchy();

    
    }
    /**
     * Gestiona l'esquiva del robot quan es detecta un enemic.
     * @param robot El robot que executa l'estat.
     */
   private void evadeTank(TeamRobot robot) {
        switch (evadeStep) {
            case 0: 
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnLeft(90);
                    evadeStep++;
                }
                break;
            case 1: 
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setAhead(100); 
                    evadeStep++;
                }
                break;
            case 2: 
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnRight(90);
                    evadeStep++;
                }
                break;
            case 3: 
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setAhead(100);
                    evadeStep++;
                }
                break;
            case 4:
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnRight(90);
                    evadeStep++;
                }
                break;
            case 5: 
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setAhead(100);
                    evadeStep++;
                }
                break;
            case 6:
                if (robot.getDistanceRemaining() == 0 && robot.getTurnRemaining() == 0) {
                    robot.setTurnLeft(90);
                    evadeStep = 0;
                    evading = false; 
                }
                break;
        }
    }
   
    /**
     * Comprova si el robot escanejat és el robot anterior en la jerarquia.
     * @param robot El robot que executa l'estat.
     * @param senderName Nom del robot que va enviar el missatge.
     * @return True si és el robot anterior, false en cas contrari.
     */
    private boolean isPreviousRobot(TeamRobot robot, String senderName) {
        List<String> robotsInOrder = hierarchy.getHierarchy();
        int currentRobotIndex = robotsInOrder.indexOf(robot.getName());
        return currentRobotIndex > 0 && robotsInOrder.get(currentRobotIndex - 1).equals(senderName);
    }

    /**
     * Mou el robot seguidor cap a la posició objectiu.
     * @param robot El robot que executa l'estat.
     */    
    private void moveFollower(TeamRobot robot) {
        
        if (target != null){

            goTo(robot, target.getX(), target.getY(),1);
            
            if (isLastRobot(robot) && hasReachedTarget(robot, target)) {
                lastRobotAtCorner = true;  

                try {
                robot.broadcastMessage("LastRobotArrived");
                } catch (IOException e) {}
            }
        }

       
    }

    /**
     * Comprova si el robot és l'últim en la jerarquia.
     * @param robot El robot que executa l'estat.
     * @return True si és l'últim robot, false en cas contrari.
     */
    private boolean isLastRobot(TeamRobot robot) {
        
        List<String> robotsInOrder = hierarchy.getHierarchy();
        return robotsInOrder.indexOf(robot.getName()) == robotsInOrder.size() - 1;
    }
    
    /**
     * Comprova si el robot és el líder de l'equip.
     * @param robot El robot que executa l'estat.
     * @return True si és el líder, false en cas contrari.
     */
    private boolean isLeader(TeamRobot robot) {
        return RobotHierarchy.getInstance().getHierarchy().get(0).equals(robot.getName());
    }
    
    /**
     * Comprova si el robot ha arribat a la posició objectiu.
     * @param robot El robot que executa l'estat.
     * @param target La posició objectiu.
     * @return True si ha arribat, false en cas contrari.
     */
    private boolean hasReachedTarget(TeamRobot robot, Point2D.Double target) {
        return Math.abs(robot.getX() - target.getX()) < CORNER_TOLERANCE &&
               Math.abs(robot.getY() - target.getY()) < CORNER_TOLERANCE;
    }

    /**
     * Mou el robot líder cap a la següent cantonada.
     * @param robot El robot que executa l'estat.
     */
    private void moveTL(TeamRobot robot){
        double battleFieldWidth = robot.getBattleFieldWidth();
        double battleFieldHeight = robot.getBattleFieldHeight();

        if (target == null || hasReachedTarget(robot, target)) target = getNextCorner(robot, battleFieldWidth, battleFieldHeight);
        goTo(robot, target.getX(), target.getY(), 1);
    }
    
    /**
     * Obté la següent cantonada per al robot.
     * @param robot El robot que executa l'estat.
     * @param fieldWidth Amplada del camp de batalla.
     * @param fieldHeight Altura del camp de batalla.
     * @return La pròxima cantonada.
     */
    private Point2D.Double getNextCorner(TeamRobot robot, double fieldWidth, double fieldHeight) {
    Point2D.Double[] corners = {
        new Point2D.Double(fieldWidth * 0.1, fieldHeight * 0.1), 
        new Point2D.Double(fieldWidth * 0.9, fieldHeight * 0.1), 
        new Point2D.Double(fieldWidth * 0.9, fieldHeight * 0.9), 
        new Point2D.Double(fieldWidth * 0.1, fieldHeight * 0.9)  
    };

    if (firstMove) {
        hierarchy.setcornerIndex(getClosestCornerIndex(robot, corners));
    }

    Point2D.Double nextCorner;
    int index = hierarchy.getcornerIndex();
    if (index >= 0) {
        System.out.println("Voy a la esquina: " + index);
        nextCorner = corners[index];
    } else {
        System.out.println("Error en el index, valor: " + index);
        nextCorner = corners[0]; // valor centinela;
    }

    
    Point2D.Double position =  new Point2D.Double(robot.getX(), robot.getY());
    double distanceToCorner = position.distance(nextCorner);
    if (distanceToCorner < 50) {  
        if (sentido_horario) {
            hierarchy.setcornerIndex((index + 1) % corners.length); 
        } else {
            hierarchy.setcornerIndex((index - 1 + 4) % corners.length); 
        }
    }

    return nextCorner;
}

    /**
     * Obtén l'índex de la cantonada més pròxima.
     * @param robot El robot que executa l'estat.
     * @param corners Les cantonades disponibles.
     * @return L'índex de la cantonada més pròxima.
     */
    public int getClosestCornerIndex(TeamRobot robot, Point2D.Double[] corners) {
        int closestCornerIndex = -1;
        double closestDistance = Double.MAX_VALUE; 

        
        double robotX = robot.getX();
        double robotY = robot.getY();

        for (int i = 0; i < corners.length; i++) {
            double distance = corners[i].distance(robotX, robotY);

            if (distance < closestDistance) {
                closestDistance = distance; 
                closestCornerIndex = i; 
            }
        }

        return closestCornerIndex; 
    }
    /**
     * Va a una posició específica.
     * @param robot El robot que executa l'estat.
     * @param x Coordenada X de la posició.
     * @param y Coordenada Y de la posició.
     * @param speed Velocitat del robot.
     */    
    private void goTo(TeamRobot robot, double x, double y, double distanceThreshold) {
        double dx = x - robot.getX();
        double dy = y - robot.getY();
        double angleToTarget = Math.atan2(dx, dy);
        double distance = Point2D.distance(robot.getX(), robot.getY(), x, y);

        if (distance > distanceThreshold) {
            double targetHeading = Math.toDegrees(angleToTarget);
            double turnAngle = robocode.util.Utils.normalRelativeAngleDegrees(targetHeading - robot.getHeading());

            
            if (Math.abs(turnAngle) > 1) {
                robot.turnRight(turnAngle);
            }

            
            double moveDistance;
            if (firstMove) {
                moveDistance = distance - distanceThreshold; 
            } else {
                moveDistance = Math.min(40, distance - distanceThreshold); 
            }

            if (moveDistance > 0) {
                robot.setAhead(moveDistance);
            }
        }
    }

    /**
    * Transmet la posició del robot a altres robots de l'equip.
    * @param robot El robot que executa l'estat.
    */
    private void broadcastPosition(TeamRobot robot) {
    try {
        
        double offsetDistance = 100;

        
        double headingRadians = Math.toRadians(robot.getHeading());  
        
        double offsetX = Math.sin(headingRadians) * offsetDistance;  
        double offsetY = Math.cos(headingRadians) * offsetDistance;  

        double previousX = robot.getX() - offsetX;
        double previousY = robot.getY() - offsetY;
        String message = "Position:" + robot.getName() + ":" + previousX + ":" + previousY;
        robot.broadcastMessage(message);
    } catch (IOException e) {}
}

    /**
    * Dibuixa elements visuals al camp de batalla.
    * @param robot El robot que executa l'estat.
    * @param g L'objecte Graphics2D utilitzat per dibuixar.
    */
    @Override
    public void onPaint(TeamRobot robot, Graphics2D g) {
        if (robot.getName().equals(hierarchy.getHierarchy().get(0))) {
            g.setColor(Color.YELLOW);
            g.drawOval((int) robot.getX() - 50, (int) robot.getY() - 50, 100, 100);
        }
    }
    
    /**
    * Gestor de l'esdeveniment quan es detecta un robot escanejat.
    * @param robot El robot que executa l'estat.
    * @param event L'esdeveniment de robot escanejat.
    */
    @Override
    public void onScannedRobot(TeamRobot robot, ScannedRobotEvent event) {
       
        if (!robot.isTeammate(event.getName())) {
            
            if (lastRobotAtCorner) {
                double enemyX = robot.getX() + Math.sin(Math.toRadians(robot.getHeading() + event.getBearing())) * event.getDistance();
                double enemyY = robot.getY() + Math.cos(Math.toRadians(robot.getHeading() + event.getBearing())) * event.getDistance();

                broadcastEnemyPosition(robot, enemyX, enemyY);

                fireAtEnemy(robot, enemyX, enemyY);
            }
        }
        double tankDistance = event.getDistance();
        if (tankDistance < 50 && !evading) {
            evading = true;
            evadeStep = 0;
        }
    }
    
    /**
     * Transmet la posició de l'enemic a altres robots de l'equip.
     * @param robot El robot que executa l'estat.
     * @param enemyX Coordenada X de la posició de l'enemic.
     * @param enemyY Coordenada Y de la posició de l'enemic.
     */
    private void broadcastEnemyPosition(TeamRobot robot, double enemyX, double enemyY) {
        try {
            String message = "EnemyPosition:" + enemyX + ":" + enemyY;
            robot.broadcastMessage(message);
        } catch (IOException e) {}
    }

    /**
    * Dispara a l'enemic en una posició específica.
    * @param robot El robot que executa l'estat.
    * @param enemyX Coordenada X de la posició de l'enemic.
    * @param enemyY Coordenada Y de la posició de l'enemic.
    */    
    private void fireAtEnemy(TeamRobot robot, double enemyX, double enemyY) {
        
        double dx = enemyX - robot.getX();
        double dy = enemyY - robot.getY();
        double angleToTarget = Math.toDegrees(Math.atan2(dx, dy));
        double turnAngle = robocode.util.Utils.normalRelativeAngleDegrees(angleToTarget - robot.getGunHeading());

        robot.turnGunRight(turnAngle);

        
        if (Math.abs(turnAngle) < 5) {
            robot.fire(1);  
        }
    }
    
    /**
     * Gestiona els esdeveniments de missatge.
     * @param robot El robot que executa l'estat.
     * @param event L'esdeveniment de missatge rebut.
     */
    @Override
    public void onMessageReceived(TeamRobot robot, MessageEvent event) {
        String msg = (String) event.getMessage();

        if (msg.startsWith("EnemyPosition:")) {
            String[] parts = msg.split(":");
            double enemyX = Double.parseDouble(parts[1]);
            double enemyY = Double.parseDouble(parts[2]);

            fireAtEnemy(robot, enemyX, enemyY);
        }

        if (msg.startsWith("Position:")) {
            String[] parts = msg.split(":");
            String robotName = parts[1];
            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);

            if (isPreviousRobot(robot, robotName)) {
                target = new Point2D.Double(x, y);
            }
        }  
        if (msg.equals("LastRobotArrived")) {
            lastRobotAtCorner = true;  // Todos los robots saben que el último ha llegado
        }     
    }
    
    /**
     * Gestiona l'esdeveniment d'impacte amb la paret.
     * @param robot El robot que executa l'estat.
     * @param event L'esdeveniment d'impacte amb la paret.
     */
    @Override
    public void OnHitWall(TeamRobot robot, HitWallEvent event) {
        robot.setBack(50);
        robot.setTurnRight(90);
    }
    
    /**
     * Gestiona l'esdeveniment de mort d'un robot.
     * @param robot El robot que executa l'estat.
     * @param event L'esdeveniment de mort d'un robot.
     */
    @Override
    public void onRobotDeath(TeamRobot robot, RobotDeathEvent event) {
        RobotHierarchy.getInstance().updateHierarchy(event.getName());
        System.out.println("JERARQUIA NUEVA" + "HA MUERTO" + event.getName());
    }
}

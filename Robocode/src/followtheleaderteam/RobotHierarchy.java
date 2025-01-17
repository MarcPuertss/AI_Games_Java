package followtheleaderteam;

import java.util.*;

/**
 * La classe RobotHierarchy gestiona la jerarquia dels robots en un equip de Robocode.
 * Permet afegir, eliminar i actualitzar la jerarquia dels robots, així com gestionar 
 * la posició a les cantonades.
 */
public class RobotHierarchy {
    private static RobotHierarchy instance; 
    private final List<String> hierarchy;
    private int cornerIndex;

    /**
     * Constructor privat per evitar la creació d'instàncies externes.
     * Inicialitza la llista de jerarquia.
     */
    private RobotHierarchy() {
        hierarchy = new ArrayList<>();
    }

    /**
     * Obtén la instància única de la classe RobotHierarchy.
     * @return L'instància de RobotHierarchy.
     */
    public static RobotHierarchy getInstance() {
        if (instance == null) {
            instance = new RobotHierarchy(); 
        }
        return instance;
    }

    /**
     * Retorna una vista no modificable de la jerarquia actual.
     * @return La jerarquia actual dels robots.
     */
    public List<String> getHierarchy() {
        return Collections.unmodifiableList(hierarchy);
    }

    /**
     * Actualitza la jerarquia en eliminar un robot mort.
     * @param deadRobot El nom del robot que ha mort.
     */
    public void updateHierarchy(String deadRobot) {
        removeRobot(deadRobot); // Elimina el robot mort
        if (hierarchy.isEmpty()) {  
            return; // No fa res si la jerarquia està buida
        }
        printHierarchy(); // Imprimeix la jerarquia actualitzada
    }

    /**
     * Estableix una nova jerarquia de robots.
     * @param newHierarchy La nova jerarquia que s'ha de configurar.
     */
    public void setHierarchy(List<String> newHierarchy) {
        hierarchy.clear(); // Esborra la jerarquia existent
        hierarchy.addAll(newHierarchy); // Afegeix la nova jerarquia
    }

    /**
     * Afegeix un robot a la jerarquia si no existeix ja.
     * @param robotName El nom del robot que s'ha d'afegir.
     */
    public void addRobot(String robotName) {
        if (!hierarchy.contains(robotName)) {
            hierarchy.add(robotName); // Afegeix el robot si no està present
        }
    }

    /**
     * Elimina un robot de la jerarquia.
     * @param robotName El nom del robot que s'ha d'eliminar.
     */
    public void removeRobot(String robotName) {
        hierarchy.remove(robotName); // Elimina el robot especificat
    }

    /**
     * Imprimeix la jerarquia actual dels robots al consola.
     */
    public void printHierarchy() {
        System.out.println("Jerarquía actual: " + hierarchy); // Mostra la jerarquia actual
    }

    /**
     * Inverteix l'ordre de la jerarquia i la imprimeix.
     */
    public void reverseHierarchy() {
        Collections.reverse(hierarchy); // Inverteix la jerarquia
        printHierarchy(); // Imprimeix la jerarquia invertida
    }

    /**
     * Estableix l'índex de cantonada.
     * @param x L'índex de cantonada a establir.
     */
    public void setcornerIndex(int x) {
        cornerIndex = x; // Estableix l'índex de cantonada
    }

    /**
     * Retorna l'índex de cantonada actual.
     * @return L'índex de cantonada actual.
     */
    public int getcornerIndex() {
        return cornerIndex; // Retorna l'índex de cantonada
    }

    /**
     * Redueix l'índex de cantonada en una quantitat específica.
     * @param x La quantitat per restar de l'índex de cantonada.
     */
    public void substractcornerIndex(int x) {
        cornerIndex = (cornerIndex - x + 4) % 4; // Redueix l'índex de cantonada
    }

    /**
     * Augmenta l'índex de cantonada en una quantitat específica.
     * @param x La quantitat per sumar a l'índex de cantonada.
     */
    public void sumcornerIndex(int x) {
        cornerIndex = (x + cornerIndex) % 4; // Augmenta l'índex de cantonada
    }
}

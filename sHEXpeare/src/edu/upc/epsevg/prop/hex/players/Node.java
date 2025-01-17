package edu.upc.epsevg.prop.hex.players;

import java.awt.Point;
/**
 * Classe que representa un node en l'algorisme de Dijkstra i altres càlculs estratègics.
 */
public class Node implements Comparable<Node> {
    public Point punto; // Coordenades del node en el tauler
    public int distancia; // Distància acumulada des de l'inici fins a aquest node
    public int prioridad; // Prioritat estratègica per avançar en el camí

    /**
     * Constructor de la classe Node.
     * 
     * @param punto      El punt que representa aquest node.
     * @param distancia La distància acumulada des del node inicial.
     */
    public Node(Point punto, int distancia) {
        this.punto = punto;
        this.distancia = distancia;

    }

    @Override
    public int compareTo(Node otro) {
        return Integer.compare(this.distancia, otro.distancia);
    }

    @Override
    public String toString() {
        return "Node{" + "punto=" + punto + ", distancia=" + distancia + ", prioridad=" + prioridad + '}';
    }
}

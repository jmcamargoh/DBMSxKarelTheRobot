import kareltherobot.*;
import java.awt.Color;

public class Minero implements Directions {
    public static void main(String[] args) {
        World.readWorld("Mina.kwld");
        World.setVisible(true);

        int numMineros = 0;
        Color negro = new Color(0, 0, 0);

        int numTrenes = 0;
        Color azul = new Color(0, 0, 255);

        int numExtractores = 0;
        Color rojo = new Color(255, 0, 0);

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-m")) {
                numMineros = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-t")) {
                numTrenes = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-e")) {
                numExtractores = Integer.parseInt(args[++i]);
            }
        }

        if (numMineros < 2)
            numMineros = 2;
        if (numTrenes < 2)
            numTrenes = 2;
        if (numExtractores < 2)
            numExtractores = 2;

        Robot mineros[] = new Robot[numMineros];
        Robot trenes[] = new Robot[numTrenes];
        Robot extractores[] = new Robot[numExtractores];

        // Crea los objetos especificados
        for (int i = 0; i < numMineros; i++) {
            Robot minero = new Robot(12 + i, 1, South, 0, negro);
            mineros[i] = minero;
            System.out.println("Se creó un objeto Minero");
        }

        for (int i = 0; i < numTrenes; i++) {
            Robot tren = new Robot(12 + i, 2, South, 0, azul);
            trenes[i] = tren;
            System.out.println("Se creó un objeto Tren");
        }

        for (int i = 0; i < numExtractores; i++) {
            Robot extractor = new Robot(12 + i, 3, South, 0, rojo);
            extractores[i] = extractor;
            System.out.println("Se creó un objeto Extractor");
        }

    }
}
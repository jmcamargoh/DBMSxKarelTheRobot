import kareltherobot.*;
import java.awt.Color;

public class Mina implements Directions {
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

        Minero mineros[] = new Minero[numMineros];
        Tren trenes[] = new Tren[numTrenes];
        Extractor extractores[] = new Extractor[numExtractores];

        // Crea los objetos especificados
        for (int i = 0; i < numMineros; i++) {
            Minero minero = new Minero(12+i, 1, South, 0, negro);
            mineros[i] = minero;
            System.out.println("Se creó un objeto Minero");
            Thread mineroThread = new Thread(minero);
        }

        for (int i = 0; i < numTrenes; i++) {
            Tren tren = new Tren(12 + i, 2, South, 0, azul);
            trenes[i] = tren;
            System.out.println("Se creó un objeto Tren");
            Thread trenThread = new Thread(tren);
        }

        for (int i = 0; i < numExtractores; i++) {
            Extractor extractor = new Extractor(12 + i, 3, South, 0, rojo);
            extractores[i] = extractor;
            System.out.println("Se creó un objeto Extractor");
            Thread extractorThread = new Thread(extractor);
        }

    }
}

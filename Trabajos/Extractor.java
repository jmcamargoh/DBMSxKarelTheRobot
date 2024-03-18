import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

public class Extractor extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();
    private CountDownLatch extractoresLatch;
    private static boolean primerMinero = true; // Variable para rastrear el primer Extractor

    public Extractor(int Street, int Avenue, Direction direction, int beepers, Color color,
            CountDownLatch extractoresLatch) {
        super(Street, Avenue, direction, beepers, color);
        this.extractoresLatch = extractoresLatch;
        World.setupThread(this);
    }

    // El robot solo puede girar a la izquierda
    public void giro(int num_reps) {
        for (int i = 0; i < num_reps; i++) {
            turnLeft();
        }
    }

    // Método para girar a la derecha (equivalente a girar tres veces a la
    // izquierda)
    public void giroDerecha() {
        giro(3);
    }

    public void giroIzquierda() {
        giro(1);
    }

    // Método para girar en sentido contrario (equivalente a girar dos veces a la
    public void cambioSentido() {
        giro(2);
    }

    // metodos recto sobrecarga, uno infinito y otro especifico
    public void recto() {
        while (frontIsClear()) {
            move();
        }
    }

    public void recto(int num_reps) {
        for (int i = 0; i < num_reps; i++) {
            move();
        }
    }

    public void entrada() {
        lock.lock();
        try {
            // Cambiate esto
            giroDerecha();
            move();
            giroIzquierda();
        } finally {
            lock.unlock();
        }
        recto();
        giroDerecha();
        move();
        extractoresLatch.countDown();
        giroIzquierda();
        lock.lock();
        try {
            if (primerMinero) {
                recto(); // Primer minero va al fondo
                giroIzquierda();
                recto(1);
                // lock.unlock();
            } else {
                recto(3); // Segundo minero va una posición menos
                // lock.unlock();
            }
        } finally {
            primerMinero = false; // Marcamos que el primer minero ya ha pasado
            lock.unlock();
        }
    }

    public void race() {
        // Las acciones del extractor
        entrada();
    }

    @Override
    public void run() {
        race();
    }
}

import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

public class Minero extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();
    private static Lock lock1 = new ReentrantLock();
    private CountDownLatch minerosLatch;
    private static boolean primerMinero = true; // Variable para rastrear el primer minero

    public Minero(int Street, int Avenue, Direction direction, int beepers, Color color, CountDownLatch minerosLatch) {
        super(Street, Avenue, direction, beepers, color);
        this.minerosLatch = minerosLatch;
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
            recto();
            giroIzquierda();
            recto(1);
        } finally {
            lock.unlock();
        }
        giroDerecha();
        recto();
        minerosLatch.countDown(); // Disminuir el contador de minerosLatch una vez que el minero comienza a
                                  // moverse
        giroDerecha();
        recto();
        giroIzquierda();
        recto();
        giroIzquierda();
        recto();
        giroIzquierda();
        recto();
        giroDerecha();

        lock.lock();
        try {
            if (primerMinero) {
                recto(); // Primer minero va al fondo
            } else {
                recto(8); // Segundo minero va una posición menos
            }
        } finally {
            primerMinero = false; // Marcamos que el primer minero ya ha pasado
            lock.unlock();
        }
    }

    public void minecraft() {
        for (int i = 0; i < 20; i++) {
            pickBeeper();
            if (primerMinero == false) {
                primerMinero = true;
            }
        }
        while (facingWest() == false) {
            giroIzquierda();
        }
        lock1.lock();
        try {
            if (primerMinero) {
                recto(5); // Primer minero va al fondo
                giroIzquierda();
                recto(1); // hacer esperar a que el segundo entregue primero
            } else {
                recto(3); // Segundo minero va una posición menos
            }
        } finally {
            primerMinero = false; // Marcamos que el primer minero ya ha pasado
            lock1.unlock();
        }

        // buscar algoritmo que me permita no estar chocandome y llevando a su punto
        // necesario
    }

    public void race() {
        // Las acciones del minero
        entrada();
        minecraft();
    }

    @Override
    public void run() {
        race();
    }
}
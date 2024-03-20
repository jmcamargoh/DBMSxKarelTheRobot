import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CountDownLatch;

public class Extractor extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();
    private static Lock lock1 = new ReentrantLock();
    private CountDownLatch extractoresLatch;
    private int identificador;
    private static boolean primerMinero = true; // Variable para rastrear el primer Extractor
    private boolean primerExtractorCompleto = false;
    private static int contador = 0; // Cuantos beepers se han dejado en el punto de extraccion

    public Extractor(int Street, int Avenue, Direction direction, int beepers, Color color,
            CountDownLatch extractoresLatch, int identificador) {
        super(Street, Avenue, direction, beepers, color);
        this.extractoresLatch = extractoresLatch;
        this.identificador = identificador;
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
            } // El segundo se queda quieto en la posicion indicada previa al lock
        } finally {
            primerMinero = false; // Marcamos que el primer minero ya ha pasado
            lock.unlock();
        }
    }

    public void extraccion_mina() {
        while (contador < 21){
            if (identificador == 1 && contador < 20) { // Primer Extractor (Dentro de la mina)
                lock1.lock();
                try {
                    recto(1);
                    cambioSentido();
                    for (int i = 0; i < 5; i++) {
                        pickBeeper();
                        contador++;
                        System.out.println("Contador = "+contador);
                    }
                    recto();
                    giroDerecha();
                    recto(5);
                    while (anyBeepersInBeeperBag()) {
                        putBeeper();
                    }
                    cambioSentido();
                    recto(2);
                } finally {
                    lock1.unlock();
                }
                recto();
                giroIzquierda();
    
            } else if (identificador == 2) { // Segundo Extractor (Para afuera de la mina)
                lock1.lock();
                try {
                    recto(1);
                    while (nextToABeeper()) {
                        pickBeeper();
                    }
                } finally {
                    lock1.unlock();
                }
                cambioSentido();
                recto();
                giroDerecha();
                recto();
                giroIzquierda();
                recto(1);
                giroDerecha();
                recto(2);
                while (anyBeepersInBeeperBag()) {
                    putBeeper();
                }
                cambioSentido();
                recto(2);
                giroIzquierda();
                recto();
                giroDerecha();
                recto();
                giroIzquierda();
            }
        }
    }

    public void race() {
        // Las acciones del extractor
        entrada();
        extraccion_mina();
    }

    @Override
    public void run() {
        race();
    }
}

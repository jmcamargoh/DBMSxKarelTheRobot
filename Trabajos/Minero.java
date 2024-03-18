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
    private static int contador = 0;    // Cuanto se ha sacado de una sola mena
    private static int menas_acabadas = 0;  // Cuantas menas se han dejado ya vacias

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
                recto(6); // Primer minero va hasta la primera mena
            } else {
                recto(5); // Segundo minero va una posición menos
                giroDerecha();
                recto(1);
                giroIzquierda();
                recto(1);
                giroIzquierda();
            }
        } finally {
            primerMinero = false; // Marcamos que el primer minero ya ha pasado
            lock.unlock();
        }
    }

    public void minecraft() {
        lock1.lock();
        while (contador <= 40) {
            if (contador < 40) {
                try {
                    if (primerMinero == false) {
                        for (int i = 0; i < 20; i++) {
                            pickBeeper();
                            contador++;
                            System.out.println(contador);
                        }
                        cambioSentido();
                        recto(1);
                        primerMinero = true;
                    } else {
                        lock.lock();
                        try {
                            recto(1);
                            giroDerecha();
                            for (int i = 0; i < 20; i++) {
                                pickBeeper();
                                contador++;
                                System.out.println(contador);
                            }
                            cambioSentido();
                            recto(1);
                        } finally {
                            lock.unlock();
                        }
                    }
                } finally {
                    lock1.unlock();
                }
    
                if (anyBeepersInBeeperBag()) {
                    for (int i = 0; i < 20; i++) {
                        putBeeper();
                    }
                    giroIzquierda();
                    recto(1);
                    giroIzquierda();
                    recto(1);
                    giroIzquierda();
                }
            } else {
                // Aqui iria todo lo de pasar a otra mena, donde se debe reiniciar el contador y debe sumar 1 a menas_acabadas
                // el problema es que no se porque no funciona correctamente la variable menas
                // esa sirve para indicarle al robot que debe moverse un pasito mas a la derecha (siguiente mena)
            }        
        }
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
import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CountDownLatch;

public class Extractor extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();
    private static Lock lock1 = new ReentrantLock();
    private static Semaphore semaforo1 = new Semaphore(1);
    private static Semaphore semaforo2 = new Semaphore(0);
    private CountDownLatch extractoresLatch;
    private int identificador;
    private static boolean primerExtractor = true; // Variable para rastrear el primer Extractor
    private boolean primerExtractorCompleto = false;
    private static int contador = 0; // Cuantos beepers se han dejado en el punto de extraccion
    private static int filas = 0; // Fila en donde almacena
    private static int columnas = 3; // Columna en donde almacena

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
        lock1.lock();
        try {
            // Cambiate esto
            giroDerecha();
            recto(1);
            giroIzquierda();
            recto();
        } finally {
            lock1.unlock();
        }
        giroDerecha();
        recto(1);
        extractoresLatch.countDown();
        giroIzquierda();
        lock.lock();
        try {
            if (primerExtractor) {
                recto(); // Primer extractor va al fondo
                giroIzquierda();
            } // El segundo se queda quieto en la posicion indicada previa al lock
        } finally {
            primerExtractor = false; // Marcamos que el primer extractor ya ha pasado
            lock.unlock();
        }
    }

    public void extraccion_mina() { // Habrá manera de optimizar la extracción?
        while (contador < 21) {
            if (identificador == 1 && contador < 20) { // Primer Extractor (Dentro de la mina)
                try {
                    Controlador_Semaforos.semaforo_extractores_4.acquire();
                    semaforo1.acquire();
                    recto(1);
                    cambioSentido();
                    for (int i = 0; i < 20; i++){
                        pickBeeper();
                        contador++;
                        System.out.println("Contador = " + contador);
                    }
                    recto();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    } finally {
                        Controlador_Semaforos.semaforo_trenes_4.release();
                    }
                giroDerecha();
                recto(5);
                while (anyBeepersInBeeperBag()) { // Entrega al segundo punto de extracción
                    putBeeper();
                }
                cambioSentido(); // Empieza el retorno a la posición inicial
                recto(2);
                semaforo2.release();
                recto();
                giroIzquierda();

            } else if (identificador == 2) { // Segundo Extractor (Para afuera de la mina)
                try {
                    semaforo2.acquire();
                    recto(1);
                    for (int i = 0; i < 20; i++) { // Recoleccion en el segundo punto de extracción
                        pickBeeper();
                    }
                    if (filas == 4) {
                        filas = 0; // Empieza en la primera fila de la próxima columna
                        columnas--; // Ya se llenó la columna, que vaya con la siguiente
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaforo1.release();
                }
            
                cambioSentido();
                recto();
                giroDerecha();
                recto();
                giroIzquierda();
                recto(1);
                giroDerecha();
                recto(1);
                giroDerecha();
                recto();
                giroIzquierda();
                recto(columnas);
                giroIzquierda();
                recto(filas);
                lock1.lock();
                try {
                    while (anyBeepersInBeeperBag()) { // Entrega en el silo vacío del almacén
                        putBeeper();
                    }
                    cambioSentido();
                    recto(filas);
                    giroDerecha();
                    recto(columnas);
                    giroDerecha();
                    recto(1);
                    giroIzquierda();
                    recto(1);
                    giroIzquierda();
                    recto();
                    giroDerecha();
                    recto();
                    giroIzquierda();
                    filas++; // Indica que esa fila ya se llenó para que siga a la próxima
                } finally {
                    lock1.unlock();
                }
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

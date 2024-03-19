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
    private static int silo = 0; // Para la visualizacion por consola

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

    public void minar() {
        while (contador <= 40) {   // Se pone la cantidad de menas que hay en cada silo (2000 en el ejercicio)
            lock1.lock();   // Lock para la consistencia de los datos compartidos (menas_acabadas y contador)
            silo = menas_acabadas+1;
            try {
                if (contador < 40) {   
                    if (primerMinero == false) { // Cuando el primero entra y saca por primera vez del primer silo, solo se hace una vez en toda la ejecucion (por su posicion inicial)
                        for (int i = 0; i < 20; i++) {  // Recoge su maxima capacidad (50 en el ejercicio)
                            pickBeeper();
                            contador++;
                            System.out.println("Beepers Extraidos: "+contador+" - Silo: "+silo);
                        }
                        cambioSentido();
                        recto(1);   // Se ubica en el punto de entrega para dejar los beepers
                        primerMinero = true;    // Habilita para que este caso no se vuelva a repetir
                    } else {       // Todos los casos siguientes al primero
                        lock.lock();    // Lock para el movimiento de los mineros (cuida que no se monten uno encima del otro)
                        try {
                            recto(1);
                            giroDerecha();
                            recto(menas_acabadas);  // Se posiciona en la mena actual a la que deben ir
                            for (int i = 0; i < 20; i++) {  // Recoge su maxima capacidad (50 en el ejercicio)
                                pickBeeper();
                                contador++; // La cantidad de beepers que han sido extraidos aumenta (para saber cuando debe cambiar de silo)
                                System.out.println("Beepers Extraidos: "+contador+" - Silo: "+silo);
                            }
                            cambioSentido();
                            recto(menas_acabadas + 1);  // Se ubica en el punto de entrega para dejar los beepers
                        } finally {
                            lock.unlock();
                        }
                    }
                } else {
                    menas_acabadas++;   // Si el silo actual ya se vacio
                    contador = 0;   // Empieza de 0 para el nuevo silo
                    if (menas_acabadas > 5) {   // En caso de que los silos queden vacios, los robots se apagan (por ahora)
                        turnOff();  // Tiene un bug extraño que literal al apagarse uno, el otro no puede terminar su ejecucion
                    }               // pero no acaba aqui el robot entonces por ahora no importa
                }
            } finally {
                lock1.unlock();
            }
    
            // Pone los beepers en el lugar de extraccion y acomoda al minero hasta que le liberen el lock y siga su trabajo
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
        }
    }

    public void race() {
        // Las acciones del minero
        entrada();
        minar();
    }

    @Override
    public void run() {
        race();
    }
}
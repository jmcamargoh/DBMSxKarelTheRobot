import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controlador_Semaforos {
    // // Sector 1
    // public static Semaphore semaforo_trenes_vertical_1 = new Semaphore(1);
    // public static Semaphore semaforo_trenes_horizontal_3 = new Semaphore(0);

    // Sector 1
    public static Semaphore semaforo_trenes_vertical_1 = new Semaphore(1); // Inicialmente permite un tren
    public static Semaphore semaforo_trenes_horizontal_3 = new Semaphore(0); // Inicialmente permite un tren

    private static Lock lockVertical = new ReentrantLock();
    private static Lock lockHorizontal = new ReentrantLock();
    private static boolean sentidoVerticalHabilitado = true; // Indica si el sentido vertical está habilitado
                                                             // actualmente

    // Método para verificar si se puede pasar en vertical en el sector 1
    public static boolean permitirPasarVerticalSector1() {
        boolean permitido = false;
        lockVertical.lock();
        try {
            permitido = sentidoVerticalHabilitado && semaforo_trenes_vertical_1.availablePermits() > 0
                    && semaforo_trenes_horizontal_3.availablePermits() == 0;
        } finally {
            lockVertical.unlock();
        }
        return permitido;
    }

    // Método para verificar si se puede pasar en horizontal en el sector 3
    public static boolean permitirPasarHorizontalSector3() {
        boolean permitido = false;
        lockHorizontal.lock();
        try {
            permitido = !sentidoVerticalHabilitado && semaforo_trenes_horizontal_3.availablePermits() > 0
                    && semaforo_trenes_vertical_1.availablePermits() == 0;
        } finally {
            lockHorizontal.unlock();
        }
        return permitido;
    }

    // Método para alternar el sentido de la intersección cada 10 segundos
    public static void alternarSentido() {
        Thread alternador = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10); // Esperar 10 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Cambiar el sentido de la intersección
                lockVertical.lock();
                lockHorizontal.lock();
                sentidoVerticalHabilitado = !sentidoVerticalHabilitado;
                lockVertical.unlock();
                lockHorizontal.unlock();
            }
        });
        alternador.setDaemon(true);
        alternador.start();
    }

    // Sector 2
    public static Semaphore semaforo_mineros_2 = new Semaphore(1);
    public static Semaphore semaforo_trenes_2 = new Semaphore(0);

    // Sector 4
    public static Semaphore semaforo_trenes_4 = new Semaphore(1);
    public static Semaphore semaforo_extractores_4 = new Semaphore(0);
}

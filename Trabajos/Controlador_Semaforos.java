import java.util.concurrent.Semaphore;

public class Controlador_Semaforos {
    
    // Sector 2
    public static Semaphore semaforo_mineros_2 = new Semaphore(1);
    public static Semaphore semaforo_trenes_2 = new Semaphore(0);

    // Sector 4
    public static Semaphore semaforo_trenes_4 = new Semaphore(1);
    public static Semaphore semaforo_extractores_4 = new Semaphore(0);
}

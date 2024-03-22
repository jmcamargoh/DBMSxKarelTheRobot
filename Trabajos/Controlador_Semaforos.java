import java.util.concurrent.Semaphore;

public class Controlador_Semaforos {
    public static Semaphore semaforo_mineros = new Semaphore(1);
    public static Semaphore semaforo_trenes = new Semaphore(0);
}

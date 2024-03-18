import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

public class Minero extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();
    private CountDownLatch minerosLatch;

    public Minero(int Street, int Avenue, Direction direction, int beepers, Color color, CountDownLatch minerosLatch){
        super(Street, Avenue, direction, beepers, color);
        this.minerosLatch = minerosLatch;
        World.setupThread(this);
    }

    // El robot solo puede girar a la izquierda
    public void giro(int num_reps){
        for (int i = 0; i < num_reps; i++){
            turnLeft();
        }
    }

    public void recto(){
        while(frontIsClear()){
            move();
        }
    }

    public void entrada(){
        lock.lock();
        try {
            recto();
            giro(1);
            move();
        } finally {
            lock.unlock();
        }
        giro(3);
        recto();
        minerosLatch.countDown(); // Disminuir el contador de minerosLatch una vez que el minero comienza a moverse
        giro(3);
        recto();
        giro(1);
        recto();
        giro(1);
        recto();
        giro(1);
        recto();
    }

    public void race(){
        // Las acciones del minero
        entrada();
    }

    @Override
    public void run(){
        race();
    }
}
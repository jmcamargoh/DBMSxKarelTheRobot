import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

public class Extractor extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();
    private CountDownLatch extractoresLatch;

    public Extractor(int Street, int Avenue, Direction direction, int beepers, Color color, CountDownLatch extractoresLatch){
        super(Street, Avenue, direction, beepers, color);
        this.extractoresLatch = extractoresLatch;
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
            // Cambiate esto
            giro(3);
            move();
            giro(1);
        } finally {
            lock.unlock();
        }
        recto();
        giro(3);
        move();
        extractoresLatch.countDown();
        giro(1);
        recto();
        giro(1);
        move();
    }

    public void race(){
        // Las acciones del extractor
        entrada();
    }

    @Override
    public void run(){
        race();
    }
}

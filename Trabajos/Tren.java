import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;

public class Tren extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();
    private CountDownLatch trenesLatch;

    public Tren(int Street, int Avenue, Direction direction, int beepers, Color color, CountDownLatch trenesLatch){
        super(Street, Avenue, direction, beepers, color);
        this.trenesLatch = trenesLatch;
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
        } finally {
            lock.unlock();
        }
        trenesLatch.countDown(); // Disminuir el contador de trenesLatch una vez que el tren comience a moverse
        giro(3);
        recto();
        giro(1);
        recto();
        giro(1);
        recto();
    }

    public void race(){
        // Las acciones del tren
        entrada();
    }

    @Override
    public void run(){
        race();
    }
}

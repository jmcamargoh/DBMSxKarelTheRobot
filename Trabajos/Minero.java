import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

public class Minero extends Robot implements Runnable {
    private static Lock lock = new ReentrantLock();

    public Minero(int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
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
        recto();
        giro(1);
        lock.lock();
        try {
            move();
            giro(3);
        } finally {
            lock.unlock();
        }
        recto();
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
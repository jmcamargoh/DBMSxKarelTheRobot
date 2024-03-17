import kareltherobot.*;
import java.awt.Color;
import java.util.concurrent.TimeUnit;

import javax.management.timer.Timer;

public class Minero extends Robot implements Runnable {
    public Minero(int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
    }

    public void race(){
        // Las acciones del minero
        while(frontIsClear()){
            move();
            TimeUnit.MILLISECONDS.sleep(1);
            if (nextToARobot()){
                turnOff();
            }
        }
    }

    @Override
    public void run(){
        race();
    }
}
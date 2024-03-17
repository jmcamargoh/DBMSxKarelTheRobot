import kareltherobot.*;
import java.awt.Color;

public class Minero extends Robot implements Runnable {
    public Minero(int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
    }

    public void race(){
        // Las acciones del minero
        move();
    }

    @Override
    public void run(){
        race();
    }
}
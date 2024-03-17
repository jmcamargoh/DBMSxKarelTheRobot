import kareltherobot.*;
import java.awt.Color;

public class Tren extends Robot implements Runnable {
    public Tren(int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
    }

    public void race(){
        // Las acciones del tren\
        move();
        move();
    }

    @Override
    public void run(){
        race();
    }
}

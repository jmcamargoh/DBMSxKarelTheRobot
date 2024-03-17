import kareltherobot.*;
import java.awt.Color;

public class Extractor extends Robot implements Runnable {
    public Extractor(int Street, int Avenue, Direction direction, int beepers, Color color){
        super(Street, Avenue, direction, beepers, color);
        World.setupThread(this);
    }

    public void race(){
        // Las acciones del extractor
        move();
    }

    @Override
    public void run(){
        race();
    }
}

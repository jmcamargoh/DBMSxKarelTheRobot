import kareltherobot.*;
import java.awt.Color;

public class Racer extends Minero implements Directions {
    public Racer(int Street, int Avenue, Direction direction, int beeps, Color color) {
        super(Street, Avenue, direction, beeps, color);
        World.setupThread(this);
    }
}

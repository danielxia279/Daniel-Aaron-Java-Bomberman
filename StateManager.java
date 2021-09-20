import java.awt.*;
import java.util.*;

/*
Contains a stack of game states as a form of polymorphism
the functions tick, draw, keyPressed, keyReleased depend on the game state
*/

public class StateManager {

    public Stack<StateTemplate> states;

    public StateManager() {
        states = new Stack<>();
        states.push(new MenuState(this));
    }

    public void tick() {
        states.peek().tick();
    }

    public void draw(Graphics g) {
        states.peek().draw(g);
    }

    public void keyPressed(int k) {
        states.peek().keyPressed(k);
    }

    public void keyReleased(int k) {
        states.peek().keyReleased(k);
    }
}

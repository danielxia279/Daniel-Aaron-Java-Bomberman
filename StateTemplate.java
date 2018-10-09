package game;

import java.awt.*;

/*
This is an abstract class that serves as the template for all game states
*/

public abstract class StateTemplate {

    protected StateManager gsm;

    public StateTemplate(StateManager gsm){
        this.gsm = gsm;
        init();
    }

    public abstract void init();
    public abstract void tick();
    public abstract void draw(Graphics g);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);
}

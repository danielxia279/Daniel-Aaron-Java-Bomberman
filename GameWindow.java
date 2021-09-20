import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
This is where the game actually runs
it manages refreshes the game 60 times every second based on the system clock
all inputs are taken here, it calls StateManager to decide which version of the action it should perform depending on the current game state and each game state has the code for the action
*/

public class GameWindow extends JPanel implements Runnable, KeyListener {
    public static final int WIDTH = 1050;
    public static final int HEIGHT = 1050;
    public static final int tileSize = 70;
    private final int FPS = 60;
    private long frameTime = 1000 / FPS;

    public GameWindow() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        setFocusable(true);
        launch();
    }

    private boolean isRunning = false;
    private Thread thread;

    private StateManager sm;

    private void launch() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        long elapsedTime, startTime, waitTime;
        sm = new StateManager();
        while (isRunning) {
            startTime = System.nanoTime();
            tick();
            repaint();
            elapsedTime = System.nanoTime() - startTime;
            waitTime = frameTime - elapsedTime / 1000000;

            if (waitTime <= 0) {
                waitTime = 5;
            }

            try {
                Thread.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
        sm.tick();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.clearRect(0, 0, WIDTH, HEIGHT);

        try {
            sm.draw(g);
        } catch (Exception e) {
        }
    }

    public void keyPressed(KeyEvent e) {
        sm.keyPressed(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        sm.keyReleased(e.getKeyCode());
    }

    public void keyTyped(KeyEvent e) {

    }

}
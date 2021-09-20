import java.awt.*;
import java.awt.event.*;

public class PauseState extends StateTemplate {

    private String[] options = { "Resume", "Restart", "Main Menu", "Quit" };
    private int current = 0;
    private double frameTime = 1000000000 / 60;
    GameState gameState;
    Color myColour = new Color(0, 0, 0, 176);

    public PauseState(StateManager gsm, GameState gameState) {
        super(gsm);
        this.gameState = gameState;
    }

    public void init() {

    }

    public void tick() {
        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                if (gameState.grid[i][j].isFire)
                    gameState.grid[i][j].FireStartTime += frameTime;
                if (gameState.grid[i][j].isBreaking)
                    gameState.grid[i][j].breakingStartTime += frameTime;
            }
        }
        for (int i = 0; i < GameState.bombList.size(); i++) {
            GameState.bombList.get(i).BombStartTime += frameTime;
        }
    }

    public void draw(Graphics g) {
        gameState.draw(g);
        g.setColor(myColour);
        g.fillRect(0, 0, 1050, 1050);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 100));
        for (int i = 0; i < options.length; i++) {
            if (i == current) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }

            g.drawString(options[i], GameWindow.WIDTH / 2 - 220, 200 + i * 120);
        }

    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_DOWN) {
            if (current < options.length - 1)
                current++;
        } else if (k == KeyEvent.VK_UP) {
            if (current > 0)
                current--;
        }
        if (k == KeyEvent.VK_ENTER) {
            if (current == 0) {
                GameState.clip.start();
                gsm.states.pop();
            } else if (current == 1) {
                gsm.states.push(new GameState(gsm));
            } else if (current == 2) {
                gsm.states.push(new MenuState(gsm));
            } else if (current == 3) {
                System.exit(0);
            }

        }
    }

    public void keyReleased(int k) {

    }
}
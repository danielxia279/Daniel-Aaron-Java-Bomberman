package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameOverState extends StateTemplate {
    private String[] options = {"Play Again", "Main Menu", "Quit"};
    private int current = 0;
    private int winner;
    private BufferedImage p1win1, p1win2, p1win3,
            p2win1, p2win2, p2win3,
            tie1, tie2, tie3;

    public GameOverState(StateManager gsm, int winner){
        super(gsm);
        this.winner = winner;
        try{
            p1win1 = ImageIO.read(new File("assets/p1win1.png"));
            p1win2 = ImageIO.read(new File("assets/p1win2.png"));
            p1win3 = ImageIO.read(new File("assets/p1win3.png"));
            p2win1 = ImageIO.read(new File("assets/p2win1.png"));
            p2win2 = ImageIO.read(new File("assets/p2win2.png"));
            p2win3 = ImageIO.read(new File("assets/p2win3.png"));
            tie1 = ImageIO.read(new File("assets/tie1.png"));
            tie2 = ImageIO.read(new File("assets/tie2.png"));
            tie3 = ImageIO.read(new File("assets/tie3.png"));
        } catch(IOException e){}

    }

    public void init(){

    }

    public void tick(){

    }

    public void draw(Graphics g){
        g.setFont(new Font("Arial", Font.ITALIC, 100));
        if(winner == 1){
            if(current == 0) g.drawImage(p1win1, 0, 0, null);
            else if(current == 1) g.drawImage(p1win2, 0, 0, null);
            else if(current == 2) g.drawImage(p1win3, 0, 0, null);
        }
        else if(winner == 2){
            if(current == 0) g.drawImage(p2win1, 0, 0, null);
            else if(current == 1) g.drawImage(p2win2, 0, 0, null);
            else if(current == 2) g.drawImage(p2win3, 0, 0, null);
        }
        else if(winner == 3) {
            if (current == 0) g.drawImage(tie1, 0, 0, null);
            else if (current == 1) g.drawImage(tie2, 0, 0, null);
            else if (current == 2) g.drawImage(tie3, 0, 0, null);
        }
    }

    public void keyPressed(int k){
        if(k == KeyEvent.VK_DOWN){
            if(current < options.length - 1) current++;
        }
        else if(k == KeyEvent.VK_UP){
            if(current > 0) current--;
        }
        if(k == KeyEvent.VK_ENTER){
            if(current == 0){
                GameState.clip.stop();
                gsm.states.push(new GameState(gsm));
            }
            else if(current == 1){
                GameState.clip.stop();
                gsm.states.push(new MenuState(gsm));
            }
            else if(current == 2){
                System.exit(0);
            }

        }
    }

    public void keyReleased(int k){

    }
}

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InstructionsState extends StateTemplate {
    private double startTime;
    private BufferedImage options[] = new BufferedImage[4], BombUpSprite, bombKickSprite, fireSprite, bombPierce,
            bombPierceSprite, lineBombSprite, powerBombSprite, skateSprite;
    private int current = 0;

    public InstructionsState(StateManager gsm) {
        super(gsm);
        try {
            options[0] = ImageIO.read(new File("assets/control1.png"));
            options[1] = ImageIO.read(new File("assets/control2.png"));
            options[2] = ImageIO.read(new File("assets/control3.png"));
            options[3] = ImageIO.read(new File("assets/control4.png"));
            BombUpSprite = ImageIO.read(new File("assets/BombUp.png"));
            bombKickSprite = ImageIO.read(new File("assets/bombKick.png"));
            fireSprite = ImageIO.read(new File("assets/fire.png"));
            bombPierceSprite = ImageIO.read(new File("assets/bombPierce.png"));
            lineBombSprite = ImageIO.read(new File("assets/lineBomb.png"));
            powerBombSprite = ImageIO.read(new File("assets/powerBomb.png"));
            skateSprite = ImageIO.read(new File("assets/skate.png"));
        } catch (IOException e) {
        }
        startTime = System.nanoTime();
    }

    public void init() {

    }

    public void tick() {

    }

    public void draw(Graphics g) {
        g.drawImage(options[current], 0, 0, null);
        if (current == 3) {
            g.drawImage(BombUpSprite, 50, 235, null);
            g.drawImage(fireSprite, 50, 310, null);
            g.drawImage(skateSprite, 50, 385, null);
            g.drawImage(powerBombSprite, 50, 460, null);
            g.drawImage(lineBombSprite, 50, 535, null);
            g.drawImage(bombPierceSprite, 50, 610, null);
            g.drawImage(bombKickSprite, 50, 685, null);
        }
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_RIGHT) {
            if (current == 3)
                gsm.states.pop();
            else
                current++;
        }
        if (k == KeyEvent.VK_LEFT) {
            if (current == 0)
                gsm.states.pop();
            else
                current--;
        }

    }

    public void keyReleased(int k) {

    }
}

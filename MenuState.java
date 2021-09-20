import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;

/*
This is the menu state
*/

public class MenuState extends StateTemplate {
    private int current = 0;
    private BufferedImage options[] = new BufferedImage[3];
    private Clip clip;

    public MenuState(StateManager gsm) {
        super(gsm);
        try {
            options[0] = ImageIO.read(new File("assets/title1.png"));
            options[1] = ImageIO.read(new File("assets/title2.png"));
            options[2] = ImageIO.read(new File("assets/title3.png"));
            playSound();
        } catch (Exception e) {
        }
    }

    public void init() {

    }

    public void tick() {

    }

    public void draw(Graphics g) {
        g.drawImage(options[current], 0, 0, null);
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
                clip.stop();
                gsm.states.push(new GameState(gsm));
            } else if (current == 1) {
                gsm.states.push(new InstructionsState(gsm));
            } else if (current == 2) {
                System.exit(0);
            }
        }
        if (k == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    public void keyReleased(int k) {

    }

    public void playSound() {
        try {
            AudioInputStream menuMusic = AudioSystem.getAudioInputStream(new File("assets/menuScreen.wav"));
            clip = AudioSystem.getClip();
            clip.open(menuMusic);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
        }
    }
}
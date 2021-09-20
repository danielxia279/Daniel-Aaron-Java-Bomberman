import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.util.*;

/*
This is the playing field game state
*/

public class GameState extends StateTemplate {

    private Player playerOne, playerTwo;
    private Random rand = new Random();
    public static ArrayList<Bomb> bombList;
    public static Tile[][] grid = new Tile[16][16]; // 1 = nothing, 2 = breakable, 4 = unbreakable
    public static int winner = 0;
    public static boolean over;
    public static double gameEndTimer;
    Color background = new Color((float) 0.207, (float) 0.439, (float) 0.2);

    public static AudioInputStream gameMusic, deathMusic;
    public static Clip clip;

    public GameState(StateManager gsm) {
        super(gsm);
    }

    public void init() {
        playSound();
        winner = 0;
        Tile.init();
        playerOne = new Player(80, 920, 1);
        playerTwo = new Player(920, 80, 2);
        bombList = new ArrayList<>();

        grid = new Tile[16][16];
        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                if (i == 1 || j == 1 || i == 15 || j == 15 || (i % 2 == 1 && j % 2 == 1)) {
                    grid[i][j] = new Tile(4, (j - 1) * 70, (i - 1) * 70);
                } else {
                    int n = (int) Math.ceil(Math.random() * 5);
                    grid[i][j] = new Tile(n == 1 ? 1 : 2, (j - 1) * 70, (i - 1) * 70);
                }
            }
        }
        grid[14][2] = new Tile(1, 70, 910);
        grid[13][2] = new Tile(1, 70, 840);
        grid[14][3] = new Tile(1, 140, 910);
        grid[2][14] = new Tile(1, 910, 70);
        grid[2][13] = new Tile(1, 840, 70);
        grid[3][14] = new Tile(1, 910, 140);
        /*
         * for (int i = 1; i<= 15; i++){ for (int j = 1; j<=15; j++){
         * System.out.print(grid[i][j].type+" "); } System.out.println(); }
         */
    }

    public void tick() {
        playerOne.tick();
        playerTwo.tick();

        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                grid[i][j].tick();
            }
        }

        for (int i = 0; i < bombList.size(); i++) {
            bombList.get(i).tick();
            if ((System.nanoTime() - bombList.get(i).BombStartTime) / 1000000 >= bombList.get(i).fuseLength) {
                int r = bombList.get(i).bombRadius, x = bombList.get(i).y / 70 + 1, y = bombList.get(i).x / 70 + 1;
                boolean p = bombList.get(i).canPierce;
                if (bombList.get(i).whoPlaced == 1)
                    playerOne.bombCount++;
                if (bombList.get(i).whoPlaced == 2)
                    playerTwo.bombCount++;
                bombList.get(i).playSound();
                bombList.remove(i);
                initialBlast(r, x, y, p);
            }
        }
        if (over) {
            gameOver(winner);
        }
    }

    public void draw(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, 1050, 1050);

        for (int i = 1; i <= 15; i++) {
            for (int j = 1; j <= 15; j++) {
                grid[i][j].draw(g);
            }
        }

        for (int i = 0; i < bombList.size(); i++) {
            bombList.get(i).draw(g);
        }
        playerOne.draw(g);
        playerTwo.draw(g);
    }

    public static boolean checkPlayer(int x, int y) {
        // need to swap x and y for graphics to grid
        x = x ^ y;
        y = y ^ x;
        x = x ^ y;
        int x1 = x / GameWindow.tileSize, y1 = y / GameWindow.tileSize, x2 = (x + 50) / GameWindow.tileSize,
                y2 = (y + 50) / GameWindow.tileSize;
        x1++;
        y1++;
        x2++;
        y2++;
        int t = 1;
        t *= grid[x1][y1].type;
        t *= grid[x1][y2].type;
        t *= grid[x2][y1].type;
        t *= grid[x2][y2].type;
        return t % 2 == 1;
    }

    public static boolean checkBomb(int x, int y) {
        // need to swap x and y for graphics to grid
        x = x ^ y;
        y = y ^ x;
        x = x ^ y;
        int x1 = x / GameWindow.tileSize, y1 = y / GameWindow.tileSize, x2 = (x + 69) / GameWindow.tileSize,
                y2 = (y + 69) / GameWindow.tileSize;
        x1++;
        y1++;
        x2++;
        y2++;

        int t = 1;
        t *= grid[x1][y1].type;
        t *= grid[x1][y2].type;
        t *= grid[x2][y1].type;
        t *= grid[x2][y2].type;
        return t % 2 == 1;
    }

    public void keyPressed(int k) {
        playerOne.keyPressed(k);
        playerTwo.keyPressed(k);
        if (k == KeyEvent.VK_P) {
            clip.stop();
            if (!playerOne.dying) {
                playerOne.right = false;
                playerOne.left = false;
                playerOne.up = false;
                playerOne.down = false;
            }
            if (!playerTwo.dying) {
                playerTwo.right = false;
                playerTwo.left = false;
                playerTwo.up = false;
                playerTwo.down = false;
            }
            gsm.states.push(new PauseState(gsm, this));
        }
    }

    public void keyReleased(int k) {
        playerOne.keyReleased(k);
        playerTwo.keyReleased(k);
    }

    private void initialBlast(int radius, int x, int y, boolean canPierce) {
        grid[x][y].startFire(false);
        grid[x][y].initial = true; // Purely for the sprites
        for (int i = 0; i < bombList.size(); i++) {
            if (bombList.get(i).y / 70 + 1 == x && bombList.get(i).x / 70 + 1 == y) {
                int r = bombList.get(i).bombRadius, x1 = bombList.get(i).y / 70 + 1, y1 = bombList.get(i).x / 70 + 1;
                boolean p = bombList.get(i).canPierce;
                if (bombList.get(i).whoPlaced == 1)
                    playerOne.bombCount++;
                if (bombList.get(i).whoPlaced == 2)
                    playerTwo.bombCount++;
                bombList.get(i).playSound();
                bombList.remove(i);
                initialBlast(r, x1, y1, p);
            }
        }
        blastUp(radius - 1, x - 1, y, canPierce);
        blastDown(radius - 1, x + 1, y, canPierce);
        blastLeft(radius - 1, x, y - 1, canPierce);
        blastRight(radius - 1, x, y + 1, canPierce);
    }

    private void blastUp(int distance, int x, int y, boolean canPierce) {
        if (distance == 0 || grid[x][y].type == 4)
            return;
        for (int i = 0; i < bombList.size(); i++) {
            if (bombList.get(i).y / 70 + 1 == x && bombList.get(i).x / 70 + 1 == y) {
                int r = bombList.get(i).bombRadius, x1 = bombList.get(i).y / 70 + 1, y1 = bombList.get(i).x / 70 + 1;
                boolean p = bombList.get(i).canPierce;
                if (bombList.get(i).whoPlaced == 1)
                    playerOne.bombCount++;
                if (bombList.get(i).whoPlaced == 2)
                    playerTwo.bombCount++;
                bombList.get(i).playSound();
                bombList.remove(i);
                initialBlast(r, x1, y1, p);
            }
        }
        if (grid[x][y].type == 2) {
            grid[x][y].breaking();
            if (rand.nextInt(100) <= 40)
                dropPower(grid[x][y]);
            if (!canPierce)
                return;
        }
        if (distance == 1 && !grid[x][y].isFire)
            grid[x][y].startFire(true);
        else
            grid[x][y].startFire(false);
        grid[x][y].up = true; // Purely for the sprites
        blastUp(distance - 1, x - 1, y, canPierce);
    }

    private void blastDown(int distance, int x, int y, boolean canPierce) {
        if (distance == 0 || grid[x][y].type == 4)
            return;
        for (int i = 0; i < bombList.size(); i++) {
            if (bombList.get(i).y / 70 + 1 == x && bombList.get(i).x / 70 + 1 == y) {
                int r = bombList.get(i).bombRadius, x1 = bombList.get(i).y / 70 + 1, y1 = bombList.get(i).x / 70 + 1;
                boolean p = bombList.get(i).canPierce;
                if (bombList.get(i).whoPlaced == 1)
                    playerOne.bombCount++;
                if (bombList.get(i).whoPlaced == 2)
                    playerTwo.bombCount++;
                bombList.get(i).playSound();
                bombList.remove(i);
                initialBlast(r, x1, y1, p);
            }
        }
        if (grid[x][y].type == 2) {
            grid[x][y].breaking();
            if (rand.nextInt(100) <= 40)
                dropPower(grid[x][y]);
            if (!canPierce)
                return;
        }
        if (distance == 1 && !grid[x][y].isFire)
            grid[x][y].startFire(true);
        else
            grid[x][y].startFire(false);
        grid[x][y].down = true; // Purely for the sprites
        blastDown(distance - 1, x + 1, y, canPierce);
    }

    private void blastLeft(int distance, int x, int y, boolean canPierce) {
        if (distance == 0 || grid[x][y].type == 4)
            return;
        for (int i = 0; i < bombList.size(); i++) {
            if (bombList.get(i).y / 70 + 1 == x && bombList.get(i).x / 70 + 1 == y) {
                int r = bombList.get(i).bombRadius, x1 = bombList.get(i).y / 70 + 1, y1 = bombList.get(i).x / 70 + 1;
                boolean p = bombList.get(i).canPierce;
                if (bombList.get(i).whoPlaced == 1)
                    playerOne.bombCount++;
                if (bombList.get(i).whoPlaced == 2)
                    playerTwo.bombCount++;
                bombList.get(i).playSound();
                bombList.remove(i);
                initialBlast(r, x1, y1, p);
            }
        }
        if (grid[x][y].type == 2) {
            grid[x][y].breaking();
            if (rand.nextInt(100) <= 40)
                dropPower(grid[x][y]);
            if (!canPierce)
                return;
        }
        if (distance == 1 && !grid[x][y].isFire)
            grid[x][y].startFire(true);
        else
            grid[x][y].startFire(false);
        grid[x][y].left = true; // Purely for the sprites
        blastLeft(distance - 1, x, y - 1, canPierce);
    }

    private void blastRight(int distance, int x, int y, boolean canPierce) {
        if (distance == 0 || grid[x][y].type == 4)
            return;
        for (int i = 0; i < bombList.size(); i++) {
            if (bombList.get(i).y / 70 + 1 == x && bombList.get(i).x / 70 + 1 == y) {
                int r = bombList.get(i).bombRadius, x1 = bombList.get(i).y / 70 + 1, y1 = bombList.get(i).x / 70 + 1;
                boolean p = bombList.get(i).canPierce;
                if (bombList.get(i).whoPlaced == 1)
                    playerOne.bombCount++;
                if (bombList.get(i).whoPlaced == 2)
                    playerTwo.bombCount++;
                bombList.get(i).playSound();
                bombList.remove(i);
                initialBlast(r, x1, y1, p);
            }
        }
        if (grid[x][y].type == 2) {
            grid[x][y].breaking();
            if (rand.nextInt(100) <= 40)
                dropPower(grid[x][y]);
            if (!canPierce)
                return;
        }
        if (distance == 1 && !grid[x][y].isFire)
            grid[x][y].startFire(true);
        else
            grid[x][y].startFire(false);
        grid[x][y].right = true; // Purely for the sprites
        blastRight(distance - 1, x, y + 1, canPierce);
    }

    private void gameOver(int winner) {
        try {
            clip.stop();
            deathMusic = AudioSystem.getAudioInputStream(new File("assets/gameover.wav"));
            clip = AudioSystem.getClip();
            clip.open(deathMusic);
            clip.start();
        } catch (Exception e) {
        }
        if (System.nanoTime() - gameEndTimer > 1500000000) {
            over = false;
            gsm.states.push(new GameOverState(gsm, winner));
        }
    }

    private void dropPower(Tile t) {
        int power = rand.nextInt(100);
        if (power <= 10)
            t.powerup = 1;
        else if (power <= 40)
            t.powerup = 2;
        else if (power <= 60)
            t.powerup = 3;
        else if (power <= 70)
            t.powerup = 4;
        else if (power <= 80)
            t.powerup = 5;
        else if (power <= 90)
            t.powerup = 6;
        else if (power <= 100)
            t.powerup = 7;
    }

    public static void playSound() {
        try {
            gameMusic = AudioSystem.getAudioInputStream(new File("assets/gameScreen.wav"));
            clip = AudioSystem.getClip();
            clip.open(gameMusic);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
        }
    }
}
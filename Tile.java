import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile {

    private int x, y;

    public int type;
    public boolean occupied = false;

    public boolean isBreaking;
    public double breakingStartTime;

    public int powerup = 0;

    public boolean isFire;
    public double FireStartTime;

    private static BufferedImage unbreakableSprite, breakableSprite, BombUpSprite, bombKickSprite, fireSprite,
            lineBombSprite, powerBombSprite, bombPierceSprite, skateSprite, breaking1, breaking2, breaking3, breaking4,
            breaking5, breaking6, up_edge1, up_edge2, up_edge3, up_edge4, up_edge5, right_edge1, right_edge2,
            right_edge3, right_edge4, right_edge5, down_edge1, down_edge2, down_edge3, down_edge4, down_edge5,
            left_edge1, left_edge2, left_edge3, left_edge4, left_edge5, initial1, initial2, initial3, initial4,
            initial5, vertical1, vertical2, vertical3, vertical4, vertical5, horizontal1, horizontal2, horizontal3,
            horizontal4, horizontal5;

    public boolean up, right, down, left, initial, edge; // Purely for the sprites

    public static void init() {
        try {
            unbreakableSprite = ImageIO.read(new File("assets/unbreakable.png"));
            breakableSprite = ImageIO.read(new File("assets/breakable.png"));
            BombUpSprite = ImageIO.read(new File("assets/BombUp.png"));
            bombKickSprite = ImageIO.read(new File("assets/bombKick.png"));
            fireSprite = ImageIO.read(new File("assets/fire.png"));
            bombPierceSprite = ImageIO.read(new File("assets/bombPierce.png"));
            lineBombSprite = ImageIO.read(new File("assets/lineBomb.png"));
            powerBombSprite = ImageIO.read(new File("assets/powerBomb.png"));
            skateSprite = ImageIO.read(new File("assets/skate.png"));
            breaking1 = ImageIO.read(new File("assets/breaking1.png"));
            breaking2 = ImageIO.read(new File("assets/breaking2.png"));
            breaking3 = ImageIO.read(new File("assets/breaking3.png"));
            breaking4 = ImageIO.read(new File("assets/breaking4.png"));
            breaking5 = ImageIO.read(new File("assets/breaking5.png"));
            breaking6 = ImageIO.read(new File("assets/breaking6.png"));
            up_edge1 = ImageIO.read(new File("assets/up_edge1.png"));
            up_edge2 = ImageIO.read(new File("assets/up_edge2.png"));
            up_edge3 = ImageIO.read(new File("assets/up_edge3.png"));

            up_edge4 = ImageIO.read(new File("assets/up_edge4.png"));
            up_edge5 = ImageIO.read(new File("assets/up_edge5.png"));
            right_edge1 = ImageIO.read(new File("assets/right_edge1.png"));
            right_edge2 = ImageIO.read(new File("assets/right_edge2.png"));
            right_edge3 = ImageIO.read(new File("assets/right_edge3.png"));
            right_edge4 = ImageIO.read(new File("assets/right_edge4.png"));
            right_edge5 = ImageIO.read(new File("assets/right_edge5.png"));
            down_edge1 = ImageIO.read(new File("assets/down_edge1.png"));
            down_edge2 = ImageIO.read(new File("assets/down_edge2.png"));
            down_edge3 = ImageIO.read(new File("assets/down_edge3.png"));
            down_edge4 = ImageIO.read(new File("assets/down_edge4.png"));
            down_edge5 = ImageIO.read(new File("assets/down_edge5.png"));
            left_edge1 = ImageIO.read(new File("assets/left_edge1.png"));
            left_edge2 = ImageIO.read(new File("assets/left_edge2.png"));
            left_edge3 = ImageIO.read(new File("assets/left_edge3.png"));
            left_edge4 = ImageIO.read(new File("assets/left_edge4.png"));
            left_edge5 = ImageIO.read(new File("assets/left_edge5.png"));
            initial1 = ImageIO.read(new File("assets/initial1.png"));
            initial2 = ImageIO.read(new File("assets/initial2.png"));
            initial3 = ImageIO.read(new File("assets/initial3.png"));
            initial4 = ImageIO.read(new File("assets/initial4.png"));
            initial5 = ImageIO.read(new File("assets/initial5.png"));
            vertical1 = ImageIO.read(new File("assets/vertical1.png"));
            vertical2 = ImageIO.read(new File("assets/vertical2.png"));
            vertical3 = ImageIO.read(new File("assets/vertical3.png"));
            vertical4 = ImageIO.read(new File("assets/vertical4.png"));
            vertical5 = ImageIO.read(new File("assets/vertical5.png"));
            horizontal1 = ImageIO.read(new File("assets/horizontal1.png"));
            horizontal2 = ImageIO.read(new File("assets/horizontal2.png"));
            horizontal3 = ImageIO.read(new File("assets/horizontal3.png"));
            horizontal4 = ImageIO.read(new File("assets/horizontal4.png"));
            horizontal5 = ImageIO.read(new File("assets/horizontal5.png"));
        } catch (IOException e) {
        }
    }

    public Tile(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;

    }

    public void startFire(boolean edge) {
        this.isFire = true;
        this.edge = edge;
        this.FireStartTime = System.nanoTime();
    }

    public void breaking() {
        isBreaking = true;
        breakingStartTime = System.nanoTime();
    }

    /*
     * 1 = increase blast radius by 1 2 = increase max bomb count by 1 3 = move
     * faster 4 = First bomb's blast radius is entire map 5 = can kick bombs 6 =
     * allows bomb to pierce 7 = places bombs in a line in front of the player
     */
    public void power(Player player) {
        if (powerup == 1)
            player.bombRadius++;
        else if (powerup == 2)
            player.bombCount++;
        else if (powerup == 3)
            player.speed++;
        else if (powerup == 4)
            player.powerBomb++;
        else if (powerup == 5)
            player.canKick = true;
        else if (powerup == 6)
            player.canPierce = true;
        else if (powerup == 7)
            player.lineBomb++;
    }

    public void tick() {
        occupied = false;
        if (isBreaking && System.nanoTime() - breakingStartTime >= 450000000) {
            type = 1;
            isBreaking = false;
        }
        if (isFire && (System.nanoTime() - FireStartTime) / 1000000 >= 450) {
            isFire = false;
            initial = false;
            up = false;
            right = false;
            down = false;
            left = false;
            edge = false;
        }
    }

    public void draw(Graphics g) {
        if (isFire) {
            if (System.nanoTime() - FireStartTime < 50000000 || System.nanoTime() - FireStartTime >= 400000000) {
                if (initial) {
                    g.drawImage(initial1, x, y, null);
                } else if (up) {
                    if (edge)
                        g.drawImage(up_edge1, x, y, null);
                    else
                        g.drawImage(vertical1, x, y, null);
                } else if (down) {
                    if (edge)
                        g.drawImage(down_edge1, x, y, null);
                    else
                        g.drawImage(vertical1, x, y, null);
                } else if (right) {
                    if (edge)
                        g.drawImage(right_edge1, x, y, null);
                    else
                        g.drawImage(horizontal1, x, y, null);
                } else if (left) {
                    if (edge)
                        g.drawImage(left_edge1, x, y, null);
                    else
                        g.drawImage(horizontal1, x, y, null);
                }
            } else if (System.nanoTime() - FireStartTime < 100000000
                    || System.nanoTime() - FireStartTime >= 350000000) {
                if (initial) {
                    g.drawImage(initial2, x, y, null);
                } else if (up) {
                    if (edge)
                        g.drawImage(up_edge2, x, y, null);
                    else
                        g.drawImage(vertical2, x, y, null);
                } else if (down) {
                    if (edge)
                        g.drawImage(down_edge2, x, y, null);
                    else
                        g.drawImage(vertical2, x, y, null);
                } else if (right) {
                    if (edge)
                        g.drawImage(right_edge2, x, y, null);
                    else
                        g.drawImage(horizontal2, x, y, null);
                } else if (left) {
                    if (edge)
                        g.drawImage(left_edge2, x, y, null);
                    else
                        g.drawImage(horizontal2, x, y, null);
                }
            } else if (System.nanoTime() - FireStartTime < 150000000
                    || System.nanoTime() - FireStartTime >= 300000000) {
                if (initial) {
                    g.drawImage(initial3, x, y, null);
                } else if (up) {
                    if (edge)
                        g.drawImage(up_edge3, x, y, null);
                    else
                        g.drawImage(vertical3, x, y, null);
                } else if (down) {
                    if (edge)
                        g.drawImage(down_edge3, x, y, null);
                    else
                        g.drawImage(vertical3, x, y, null);
                } else if (right) {
                    if (edge)
                        g.drawImage(right_edge3, x, y, null);
                    else
                        g.drawImage(horizontal3, x, y, null);
                } else if (left) {
                    if (edge)
                        g.drawImage(left_edge3, x, y, null);
                    else
                        g.drawImage(horizontal3, x, y, null);
                }
            } else if (System.nanoTime() - FireStartTime < 200000000
                    || System.nanoTime() - FireStartTime >= 250000000) {
                if (initial) {
                    g.drawImage(initial4, x, y, null);
                } else if (up) {
                    if (edge)
                        g.drawImage(up_edge4, x, y, null);
                    else
                        g.drawImage(vertical4, x, y, null);
                } else if (down) {
                    if (edge)
                        g.drawImage(down_edge4, x, y, null);
                    else
                        g.drawImage(vertical4, x, y, null);
                } else if (right) {
                    if (edge)
                        g.drawImage(right_edge4, x, y, null);
                    else
                        g.drawImage(horizontal4, x, y, null);
                } else if (left) {
                    if (edge)
                        g.drawImage(left_edge4, x, y, null);
                    else
                        g.drawImage(horizontal4, x, y, null);
                }
            } else if (System.nanoTime() - FireStartTime < 250000000) {
                if (initial) {
                    g.drawImage(initial5, x, y, null);
                } else if (up) {
                    if (edge)
                        g.drawImage(up_edge5, x, y, null);
                    else
                        g.drawImage(vertical5, x, y, null);
                } else if (down) {
                    if (edge)
                        g.drawImage(down_edge5, x, y, null);
                    else
                        g.drawImage(vertical5, x, y, null);
                } else if (right) {
                    if (edge)
                        g.drawImage(right_edge5, x, y, null);
                    else
                        g.drawImage(horizontal5, x, y, null);
                } else if (left) {
                    if (edge)
                        g.drawImage(left_edge5, x, y, null);
                    else
                        g.drawImage(horizontal5, x, y, null);
                }
            }
        }

        if (powerup == 1) {
            g.drawImage(fireSprite, x, y, null);
        }
        if (powerup == 2) {
            g.drawImage(BombUpSprite, x, y, null);
        }
        if (powerup == 3) {
            g.drawImage(skateSprite, x, y, null);
        }
        if (powerup == 4) {
            g.drawImage(powerBombSprite, x, y, null);
        }
        if (powerup == 5) {
            g.drawImage(bombKickSprite, x, y, null);
        }
        if (powerup == 6) {
            g.drawImage(bombPierceSprite, x, y, null);
        }
        if (powerup == 7) {
            g.drawImage(lineBombSprite, x, y, null);
        }
        if (type == 4) {
            g.drawImage(unbreakableSprite, x, y, null);
        } else if (isBreaking) {
            if (System.nanoTime() - breakingStartTime < 75000000)
                g.drawImage(breaking1, x, y, null);
            else if (System.nanoTime() - breakingStartTime < 150000000)
                g.drawImage(breaking2, x, y, null);
            else if (System.nanoTime() - breakingStartTime < 225000000)
                g.drawImage(breaking3, x, y, null);
            else if (System.nanoTime() - breakingStartTime < 300000000)
                g.drawImage(breaking4, x, y, null);
            else if (System.nanoTime() - breakingStartTime < 375000000)
                g.drawImage(breaking5, x, y, null);
            else if (System.nanoTime() - breakingStartTime < 450000000)
                g.drawImage(breaking6, x, y, null);
        } else if (type == 2) {
            g.drawImage(breakableSprite, x, y, null);
        }
    }
}
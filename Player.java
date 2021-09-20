import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    public boolean right, left, up, down;
    public boolean dying;
    public int x, y, speed = 4, powerBomb = 0, lineBomb = 0;
    private int startWalkingx, startWalkingy;
    public ArrayList<Integer> direction = new ArrayList<>(); // 1 = up, 2 = right, 3 = down, 4 = left
    public boolean canKick, canPierce;
    public int type;
    public int bombRadius = 2, fuseLength = 2200; // milliseconds
    private BufferedImage up1, up2, up3, right1, right2, right3, down1, down2, down3, left1, left2, left3, death1,
            death2, death3, death4, death5, death6;
    public double movingStartTime, dyingStartTime;
    public int bombCount = 1;

    public Player(int x, int y, int t) {
        this.x = x;
        this.y = y;
        this.type = t;
        direction.add(this.type == 1 ? 1 : 3);
        try {
            up1 = ImageIO.read(new File(t == 1 ? "assets/red_up1.png" : "assets/blue_up1.png"));
            up2 = ImageIO.read(new File(t == 1 ? "assets/red_up2.png" : "assets/blue_up2.png"));
            up3 = ImageIO.read(new File(t == 1 ? "assets/red_up3.png" : "assets/blue_up3.png"));
            right1 = ImageIO.read(new File(t == 1 ? "assets/red_right1.png" : "assets/blue_right1.png"));
            right2 = ImageIO.read(new File(t == 1 ? "assets/red_right2.png" : "assets/blue_right2.png"));
            right3 = ImageIO.read(new File(t == 1 ? "assets/red_right3.png" : "assets/blue_right3.png"));
            down1 = ImageIO.read(new File(t == 1 ? "assets/red_down1.png" : "assets/blue_down1.png"));
            down2 = ImageIO.read(new File(t == 1 ? "assets/red_down2.png" : "assets/blue_down2.png"));
            down3 = ImageIO.read(new File(t == 1 ? "assets/red_down3.png" : "assets/blue_down3.png"));
            left1 = ImageIO.read(new File(t == 1 ? "assets/red_left1.png" : "assets/blue_left1.png"));
            left2 = ImageIO.read(new File(t == 1 ? "assets/red_left2.png" : "assets/blue_left2.png"));
            left3 = ImageIO.read(new File(t == 1 ? "assets/red_left3.png" : "assets/blue_left3.png"));
            death1 = ImageIO.read(new File(t == 1 ? "assets/red_death1.png" : "assets/blue_death1.png"));
            death2 = ImageIO.read(new File(t == 1 ? "assets/red_death2.png" : "assets/blue_death2.png"));
            death3 = ImageIO.read(new File(t == 1 ? "assets/red_death3.png" : "assets/blue_death3.png"));
            death4 = ImageIO.read(new File(t == 1 ? "assets/red_death4.png" : "assets/blue_death4.png"));
            death5 = ImageIO.read(new File(t == 1 ? "assets/red_death5.png" : "assets/blue_death5.png"));
            death6 = ImageIO.read(new File(t == 1 ? "assets/red_death6.png" : "assets/blue_death6.png"));
        } catch (IOException e) {
        }
    }

    public void tick() {
        if (right) {
            moveRight(speed);
        }
        if (left) {
            moveLeft(speed);
        }
        if (up) {
            moveUp(speed);
        }
        if (down) {
            moveDown(speed);
        }
        if (!right && !left && !up && !down)
            movingStartTime = System.nanoTime();
        if (GameState.grid[(y + 25) / GameWindow.tileSize + 1][(x + 25) / GameWindow.tileSize + 1].isFire) {
            if (!dying) {
                GameState.winner += type == 1 ? 2 : 1;
                GameState.gameEndTimer = System.nanoTime();
            }
            GameState.over = true;
            right = false;
            left = false;
            up = false;
            down = false;
            if (!dying)
                dyingStartTime = System.nanoTime();
            dying = true;
        }
        if (GameState.grid[(y + 25) / GameWindow.tileSize + 1][(x + 25) / GameWindow.tileSize + 1].powerup != 0) {
            GameState.grid[(y + 25) / GameWindow.tileSize + 1][(x + 25) / GameWindow.tileSize + 1].power(this);
            GameState.grid[(y + 25) / GameWindow.tileSize + 1][(x + 25) / GameWindow.tileSize + 1].powerup = 0;
        }
    }

    private void lineBomb(int x1, int y1) {
        if (GameState.grid[x1][y1].type != 1)
            return;
        if (!GameState.grid[x1][y1].occupied) {
            if (powerBomb > 0)
                GameState.bombList.add(new Bomb(fuseLength, 15, this, (y1 - 1) * 70, (x1 - 1) * 70, canPierce));
            else
                GameState.bombList.add(new Bomb(fuseLength, bombRadius, this, (y1 - 1) * 70, (x1 - 1) * 70, canPierce));
        }
        if (direction.get(direction.size() - 1) == 1)
            lineBomb(x1 - 1, y1);
        if (direction.get(direction.size() - 1) == 2)
            lineBomb(x1, y1 + 1);
        if (direction.get(direction.size() - 1) == 3)
            lineBomb(x1 + 1, y1);
        if (direction.get(direction.size() - 1) == 4)
            lineBomb(x1, y1 - 1);
    }

    private static boolean intersects(int p1, int p2) {
        return (Math.abs(p2 - p1) <= 50);
    }

    private void moveRight(int n) {
        for (int i = 0; i < GameState.bombList.size(); i++) {
            if (x + 50 == GameState.bombList.get(i).x && intersects(y, GameState.bombList.get(i).y)) {
                if (canKick)
                    GameState.bombList.get(i).right = true;
                return;
            }
        }
        while (n-- > 0 && GameState.checkPlayer(x + 1, y)) {
            x++;
            for (int i = 0; i < GameState.bombList.size(); i++) {
                if (x + 50 == GameState.bombList.get(i).x && intersects(y, GameState.bombList.get(i).y)) {
                    if (canKick)
                        GameState.bombList.get(i).right = true;
                    return;
                }
            }
        }
    }

    private void moveLeft(int n) {
        for (int i = 0; i < GameState.bombList.size(); i++) {
            if (x == GameState.bombList.get(i).x + 70 && intersects(y, GameState.bombList.get(i).y)) {
                if (canKick)
                    GameState.bombList.get(i).left = true;
                return;
            }
        }
        while (n-- > 0 && GameState.checkPlayer(x - 1, y)) {
            x--;
            for (int i = 0; i < GameState.bombList.size(); i++) {
                if (x == GameState.bombList.get(i).x + 70 && intersects(y, GameState.bombList.get(i).y)) {
                    if (canKick)
                        GameState.bombList.get(i).left = true;
                    return;
                }
            }
        }
    }

    private void moveUp(int n) {
        for (int i = 0; i < GameState.bombList.size(); i++) {
            if (y == GameState.bombList.get(i).y + 70 && intersects(x, GameState.bombList.get(i).x)) {
                if (canKick)
                    GameState.bombList.get(i).up = true;
                return;
            }
        }
        while (n-- > 0 && GameState.checkPlayer(x, y - 1)) {
            y--;
            for (int i = 0; i < GameState.bombList.size(); i++) {
                if (y == GameState.bombList.get(i).y + 70 && intersects(x, GameState.bombList.get(i).x)) {
                    if (canKick)
                        GameState.bombList.get(i).up = true;
                    return;
                }
            }
        }
    }

    private void moveDown(int n) {
        for (int i = 0; i < GameState.bombList.size(); i++) {
            if (y + 50 == GameState.bombList.get(i).y && intersects(x, GameState.bombList.get(i).x)) {
                if (canKick)
                    GameState.bombList.get(i).down = true;
                return;
            }
        }
        while (n-- > 0 && GameState.checkPlayer(x, y + 1)) {
            y++;
            for (int i = 0; i < GameState.bombList.size(); i++) {
                if (y + 50 == GameState.bombList.get(i).y && intersects(x, GameState.bombList.get(i).x)) {
                    if (canKick)
                        GameState.bombList.get(i).down = true;
                    return;
                }
            }
        }
    }

    public void draw(Graphics g) {
        if (dying) {
            if (System.nanoTime() - dyingStartTime < 500000000)
                g.drawImage(death1, x, y - 35, null);
            else if (System.nanoTime() - dyingStartTime < 700000000)
                g.drawImage(death2, x, y - 35, null);
            else if (System.nanoTime() - dyingStartTime < 900000000)
                g.drawImage(death3, x, y - 35, null);
            else if (System.nanoTime() - dyingStartTime < 1100000000)
                g.drawImage(death4, x, y - 35, null);
            else if (System.nanoTime() - dyingStartTime < 1300000000)
                g.drawImage(death5, x, y - 35, null);
            else if (System.nanoTime() - dyingStartTime < 1800000000)
                g.drawImage(death6, x, y - 35, null);
        } else {
            if (right || left || up || down) {
                if (direction.get(direction.size() - 1) == 1) {
                    if (Math.abs(startWalkingy - y) % 200 < 50)
                        g.drawImage(up2, x, y - 35, null);
                    else if (Math.abs(startWalkingy - y) % 200 < 100)
                        g.drawImage(up1, x, y - 35, null);
                    else if (Math.abs(startWalkingy - y) % 200 < 150)
                        g.drawImage(up3, x, y - 35, null);
                    else if (Math.abs(startWalkingy - y) % 200 < 200)
                        g.drawImage(up1, x, y - 35, null);
                }
                if (direction.get(direction.size() - 1) == 2) {
                    if (Math.abs(startWalkingx - x) % 200 < 50)
                        g.drawImage(right2, x, y - 35, null);
                    else if (Math.abs(startWalkingx - x) % 200 < 100)
                        g.drawImage(right1, x, y - 35, null);
                    else if (Math.abs(startWalkingx - x) % 200 < 150)
                        g.drawImage(right3, x, y - 35, null);
                    else if (Math.abs(startWalkingx - x) % 200 < 200)
                        g.drawImage(right1, x, y - 35, null);
                }
                if (direction.get(direction.size() - 1) == 3) {
                    if (Math.abs(startWalkingy - y) % 200 < 50)
                        g.drawImage(down2, x, y - 35, null);
                    else if (Math.abs(startWalkingy - y) % 200 < 100)
                        g.drawImage(down1, x, y - 35, null);
                    else if (Math.abs(startWalkingy - y) % 200 < 150)
                        g.drawImage(down3, x, y - 35, null);
                    else if (Math.abs(startWalkingy - y) % 200 < 200)
                        g.drawImage(down1, x, y - 35, null);
                }
                if (direction.get(direction.size() - 1) == 4) {
                    if (Math.abs(startWalkingx - x) % 200 < 50)
                        g.drawImage(left2, x, y - 35, null);
                    else if (Math.abs(startWalkingx - x) % 200 < 100)
                        g.drawImage(left1, x, y - 35, null);
                    else if (Math.abs(startWalkingx - x) % 200 < 150)
                        g.drawImage(left3, x, y - 35, null);
                    else if (Math.abs(startWalkingx - x) % 200 < 200)
                        g.drawImage(left1, x, y - 35, null);
                }
            } else {
                if (direction.get(direction.size() - 1) == 1)
                    g.drawImage(up1, x, y - 35, null);
                if (direction.get(direction.size() - 1) == 2)
                    g.drawImage(right1, x, y - 35, null);
                if (direction.get(direction.size() - 1) == 3)
                    g.drawImage(down1, x, y - 35, null);
                if (direction.get(direction.size() - 1) == 4)
                    g.drawImage(left1, x, y - 35, null);
            }
        }
    }

    public void keyPressed(int k) {
        if (!dying) {
            if (k == KeyEvent.VK_D && type == 1) {
                if (right || left || up || down) {
                    if (direction.indexOf(2) == -1) {
                        direction.add(2);
                        startWalkingx = x;
                    }
                } else
                    direction.set(0, 2);
                right = true;
            }
            if (k == KeyEvent.VK_A && type == 1) {
                if (right || left || up || down) {
                    if (direction.indexOf(4) == -1) {
                        direction.add(4);
                        startWalkingx = x;
                    }
                } else
                    direction.set(0, 4);
                left = true;
            }
            if (k == KeyEvent.VK_W && type == 1) {
                if (right || left || up || down) {
                    if (direction.indexOf(1) == -1) {
                        direction.add(1);
                        startWalkingy = y;
                    }
                } else
                    direction.set(0, 1);
                up = true;
            }
            if (k == KeyEvent.VK_S && type == 1) {
                if (right || left || up || down) {
                    if (direction.indexOf(3) == -1) {
                        direction.add(3);
                        startWalkingy = y;
                    }
                } else
                    direction.set(0, 3);
                down = true;
            }
            if (k == KeyEvent.VK_Q && type == 1 && bombCount > 0
                    && !GameState.grid[(y + 25) / GameWindow.tileSize + 1][(x + 25) / GameWindow.tileSize
                            + 1].occupied) {
                if (lineBomb > 0) {
                    lineBomb(y / GameWindow.tileSize + 1, x / GameWindow.tileSize + 1);
                    bombCount++;
                    if (powerBomb > 0)
                        powerBomb--;
                    lineBomb--;
                } else if (powerBomb > 0) {
                    GameState.bombList
                            .add(new Bomb(fuseLength, 15, this, ((x + 25) / 70) * 70, ((y + 25) / 70) * 70, canPierce));
                    powerBomb--;
                } else
                    GameState.bombList.add(new Bomb(fuseLength, bombRadius, this, ((x + 25) / 70) * 70,
                            ((y + 25) / 70) * 70, canPierce));
            }
            if (k == KeyEvent.VK_L && type == 2) {
                if (right || left || up || down) {
                    if (direction.indexOf(2) == -1) {
                        direction.add(2);
                        startWalkingx = x;
                    }
                } else
                    direction.set(0, 2);
                right = true;
            }
            if (k == KeyEvent.VK_J && type == 2) {
                if (right || left || up || down) {
                    if (direction.indexOf(4) == -1) {
                        direction.add(4);
                        startWalkingx = x;
                    }
                } else
                    direction.set(0, 4);
                left = true;
            }
            if (k == KeyEvent.VK_I && type == 2) {
                if (right || left || up || down) {
                    if (direction.indexOf(1) == -1) {
                        direction.add(1);
                        startWalkingy = y;
                    }
                } else
                    direction.set(0, 1);
                up = true;
            }
            if (k == KeyEvent.VK_K && type == 2) {
                if (right || left || up || down) {
                    if (direction.indexOf(3) == -1) {
                        direction.add(3);
                        startWalkingy = y;
                    }
                } else
                    direction.set(0, 3);
                down = true;
            }
            if (k == KeyEvent.VK_U && type == 2 && bombCount > 0
                    && !GameState.grid[y / GameWindow.tileSize + 1][x / GameWindow.tileSize + 1].occupied) {
                if (lineBomb > 0) {
                    lineBomb(y / GameWindow.tileSize + 1, x / GameWindow.tileSize + 1);
                    bombCount++;
                    if (powerBomb > 0)
                        powerBomb--;
                    lineBomb--;
                } else if (powerBomb > 0) {
                    GameState.bombList
                            .add(new Bomb(fuseLength, 15, this, ((x + 25) / 70) * 70, ((y + 25) / 70) * 70, canPierce));
                    powerBomb--;
                } else
                    GameState.bombList.add(new Bomb(fuseLength, bombRadius, this, ((x + 25) / 70) * 70,
                            ((y + 25) / 70) * 70, canPierce));
            }
        }
    }

    public void keyReleased(int k) {
        if (k == KeyEvent.VK_D && type == 1) {
            right = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(2));
        }
        if (k == KeyEvent.VK_A && type == 1) {
            left = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(4));
        }
        if (k == KeyEvent.VK_W && type == 1) {
            up = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(1));
        }
        if (k == KeyEvent.VK_S && type == 1) {
            down = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(3));
        }
        if (k == KeyEvent.VK_L && type == 2) {
            right = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(2));
        }
        if (k == KeyEvent.VK_J && type == 2) {
            left = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(4));
        }
        if (k == KeyEvent.VK_I && type == 2) {
            up = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(1));
        }
        if (k == KeyEvent.VK_K && type == 2) {
            down = false;
            if (direction.size() > 1)
                direction.remove(direction.indexOf(3));
        }
    }
}
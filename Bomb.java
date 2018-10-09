package game;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Bomb {

    private BufferedImage sprite1, sprite2, sprite3, sprite4;

    public int x, y;
    public boolean left, right, up, down, canPierce;
    public int velocity = 5;
    public int fuseLength;
    public double BombStartTime; //nanoseconds
    public int bombRadius;
    public int whoPlaced;

    public Bomb(int fuseLength, int bombRadius, Player player, int x, int y, boolean canPierce){
        this.fuseLength = fuseLength;
        this.BombStartTime = System.nanoTime();
        this.bombRadius = bombRadius;
        this.whoPlaced = player.type;
        player.bombCount--;
        this.x = x;
        this.y = y;
        this.canPierce = canPierce;

        try {
            sprite2 = ImageIO.read(new File(bombRadius==15?"assets/power_bomb1.png":canPierce?"assets/pierce_bomb1.png":"assets/bomb1.png"));
            sprite1 = ImageIO.read(new File(bombRadius==15?"assets/power_bomb2.png":canPierce?"assets/pierce_bomb2.png":"assets/bomb2.png"));
            sprite3 = ImageIO.read(new File(bombRadius==15?"assets/power_bomb3.png":canPierce?"assets/pierce_bomb3.png":"assets/bomb3.png"));
            sprite4 = ImageIO.read(new File(bombRadius==15?"assets/power_bomb4.png":canPierce?"assets/pierce_bomb4.png":"assets/bomb4.png"));
        }
        catch(IOException e){}
    }

    public void tick(){
        if(right){
            moveRight(velocity);
        }
        if(left){
            moveLeft(velocity);
        }
        if(up){
            moveUp(velocity);
        }
        if(down){
            moveDown(velocity);
        }
        GameState.grid[y/70+1][x/70+1].occupied = true;
    }
    public void playSound(){
        try{
            AudioInputStream explosionSound = AudioSystem.getAudioInputStream(new File("assets/explosion.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(explosionSound);
            clip.start();
        }
        catch(Exception e){}
    }
    public void draw(Graphics g){
        if((System.nanoTime() - BombStartTime) % 1000000000 < 400000000) g.drawImage(sprite1, x, y, null);
        else if((System.nanoTime() - BombStartTime) % 1000000000 < 600000000) g.drawImage(sprite2, x, y, null);
        else if((System.nanoTime() - BombStartTime) % 1000000000 < 800000000) g.drawImage(sprite3, x, y, null);
        else if((System.nanoTime() - BombStartTime) % 1000000000 < 1000000000) g.drawImage(sprite4, x, y, null);
    }
    private void moveRight(int n){
        while(n-->0&&GameState.checkBomb(x+1, y)){
            x++;
        }
    }
    private void moveLeft(int n){
        while(n-->0&&GameState.checkBomb(x-1, y)){
            x--;
        }
    }
    private void moveUp(int n){
        while(n-->0&&GameState.checkBomb(x, y-1)){
            y--;
        }
    }
    private void moveDown(int n){
        while(n-->0&&GameState.checkBomb(x, y+1)){
            y++;
        }
    }
}
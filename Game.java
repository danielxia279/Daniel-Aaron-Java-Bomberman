package game;
import java.util.*;
public class Game {
    public static int [][] map = new int[16][14]; //0 = empty, 1 = breakable, 2 = unbreakable
    public static player p1, p2;
    public static void fillMap(){
        for (int i = 1; i<=15; i++){
            for (int j = 1; j<=13; j++){
                if (i==1||j==1||i==15||j==13||(i%2==1&&j%2==1)){
                    map[i][j]=2;
                }
                else{
                    int n = (int)Math.ceil(Math.random()*5);
                    map[i][j]=n==1?0:1;
                }
            }
        }
    }
    public static void main(String[] args) {
       
        
    }
    public static class player{
        float x, y, velocity;
        player(float x, float y){
            this.x=x;this.y=y;
            velocity = 5;
        }
    }
}

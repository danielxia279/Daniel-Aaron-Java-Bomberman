package game;
public class Game {
    public static int [][] map;
    public static player p1, p2;
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

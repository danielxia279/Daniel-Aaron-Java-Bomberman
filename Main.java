import java.awt.*;
import javax.swing.*;

/*
This is where the game begins
Here, the game window is initialized
*/

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bomber Person");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(new GameWindow(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
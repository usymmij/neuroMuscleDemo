package Pages;

import java.awt.*;

public class Menu extends Page {

    public Menu() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", 0, 30));
        g.drawString("Change colour theme", 500, 150);
    }
}
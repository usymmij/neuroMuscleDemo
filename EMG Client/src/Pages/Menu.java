package Pages;

import java.awt.*;

public class Menu extends Page {

    public Menu() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 0, 30));
        g.drawString("connect to arduino", 500, 150);
    }
}
package Pages;

import javax.swing.*;
import java.awt.*;

public class Page extends JComponent {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 0, 50));

        //exit box
        g.drawLine(getWidth() - 30, 10, getWidth() - 10, 30);
        g.drawLine(getWidth() - 10, 10, getWidth() - 30, 30);

        //minimize
        g.drawLine(getWidth() - 75, 20, getWidth() - 55, 20);

        //menu button
        g.drawLine( 10, 10, 40, 10);
        g.drawLine( 10, 20, 40, 20);
        g.drawLine( 10, 30, 40, 30);
    }
}

package Pages;

import java.awt.*;

public class Menu extends Page {

    public Menu() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < 5; i++) {
            g.fillRect(getWidth() / 2 - 500,
                    ((getHeight() / 8) * (i + 1)) + (i * 50),
                    1000, 100);
        }
        //remember to add auto text removal for the current mode in the future
        g.setColor(Color.BLACK);
        g.drawString("Uninterpolated data", getWidth() / 2 - 480,
                (getHeight() / 8) + 70);
        g.drawString("derivative data", getWidth() / 2 - 480,
                (getHeight() / 8 * 2) + 120);
        g.drawString("add servo to pin 12", getWidth() / 2 - 480,
                (getHeight() / 8 * 3) + 175);
        g.drawString("switch input method (audio/serial)", getWidth() / 2 - 480,
                (getHeight() / 8 * 4) + 225);
        g.drawString("change colour scheme", getWidth() / 2 - 480,
                (getHeight() / 8 * 5) + 275);
    }
}
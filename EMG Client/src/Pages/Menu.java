package Pages;

import java.awt.*;

public class Menu extends Page {
    String stringSlotA;
    String stringSlotB;

    public Menu(String current) {
        if(current.equals("derivative data")) {
            stringSlotA = "uninterpolated data";
            stringSlotB = "interpolated data";
        } else if(current.equals("uninterpolated data")) {
            stringSlotA = "interpolated data";
            stringSlotB = "derivative data";
        } else{
            stringSlotA = "uninterpolated data";
            stringSlotB = "derivative data";
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < 6; i++) {
            g.fillRect(getWidth() / 2 - 500,
                    ((getHeight() / 10) * (i + 1)) + (i * 10),
                    1000, 100);
        }
        //remember to add auto text removal for the current mode in the future
        g.setColor(Color.BLACK);
        g.drawString(stringSlotA, getWidth() / 2 - 480,
                (getHeight() / 10) + 70);
        g.drawString(stringSlotB, getWidth() / 2 - 480,
                (getHeight() / 10 * 2) + 90);
        g.drawString("add servo to pin 12", getWidth() / 2 - 480,
                (getHeight() / 10 * 3) + 100);
        g.drawString("switch input method (audio/serial)", getWidth() / 2 - 480,
                (getHeight() / 10 * 4) + 110);
        g.drawString("change colour scheme", getWidth() / 2 - 480,
                (getHeight() / 10 * 5) + 120);
        g.drawString("Reconnect arduino", getWidth() / 2 - 480,
                (getHeight() / 10 * 6) + 130);
    }
}
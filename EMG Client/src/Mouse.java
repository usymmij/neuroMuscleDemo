import javafx.scene.input.MouseButton;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class Mouse implements MouseListener {
    private static int x, y = 0;
    private static int mouseButton = 0;

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    @Override
    public void mousePressed(MouseEvent e){
        x = e.getX();
        y = e.getY();
        mouseButton = e.getButton();
        if(UI.getState() == UI.Displayed.MENU) {
            menuOptions();
        }
        hotbarClickedOptions();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void menuOptions() {
        Serial.setPort();
    }

    private void hotbarClickedOptions() {
        if(mouseButton == MouseEvent.BUTTON1) {
            if (y < 35) {
                if (x > UI.getWidth() - 42) {//clicked the x button
                    Exit.normal();
                } else if(x > UI.getWidth() - 132) {// clicked the minimize(-) button
                    UI.minimize();
                } else if(x < 50) {// clicked menu button
                    if(UI.getState() == UI.Displayed.MENU) {
                        UI.change(UI.Displayed.ALL);
                    } else {
                        UI.change(UI.Displayed.MENU);
                    }
                }
            }
        }
    }
}

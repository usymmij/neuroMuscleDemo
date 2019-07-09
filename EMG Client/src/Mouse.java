import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
    private static int x, y = 0;
    private static int mouseButton = 0;

    private static UI.Displayed lastState;

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
        if (y < 35) {
            hotbarClickedOptions();
        }
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
        if(x > UI.getWidth() / 2 - 500 && x < UI.getWidth() / 2 + 500) {//buttons
            int baseHeight = UI.getHeight() / 10;
            if(y > baseHeight + 10 && y < baseHeight + 10 + 100)  {//button 0

            } else if(y > baseHeight * 2 + 30 && y < baseHeight * 2 + 20 + 100)  {//button 1

            } else if(y > baseHeight * 3 + 30 && y < baseHeight * 3 + 30 + 100)  {//button 2

            } else if(y > baseHeight * 4 + 40 && y < baseHeight * 4 + 40 + 100)  {//button 3

            } else if(y > baseHeight * 5 + 50 && y < baseHeight * 5 + 50 + 100)  {//button 4

            } else if(y > baseHeight * 6 + 60 && y < baseHeight * 6 + 60 + 100)  {//button 5
                CommThread.setMode(CommThread.CommThreadMode.IDLE);
                Serial.closePort();
                UI.change(lastState);
            }
        }
    }

    private void hotbarClickedOptions() {
                if (x > UI.getWidth() - 42) {//clicked the x button
                    Exit.normal();
                } else if(x > UI.getWidth() - 132) {// clicked the minimize(-) button
                    UI.minimize();
                } else if(x < 50) {// clicked menu button
                    if (UI.getState() == UI.Displayed.MENU) {
                        UI.change(lastState);
                    } else {
                        lastState = UI.getState();
                        UI.change(UI.Displayed.MENU);
                    }
                }
                UI.update();
    }
}

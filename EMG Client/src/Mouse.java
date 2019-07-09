import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
    private static int x, y = 0;
    private static int mouseButton = 0;
    private static UI.Displayed typeSlotA = UI.Displayed.RAW;
    private static UI.Displayed typeSlotB = UI.Displayed.DERIVATIVE;

    public static UI.Displayed lastState;
    public static UI.Displayed lastAllState;

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
                UI.change(typeSlotA);
                if(typeSlotA == UI.Displayed.RAW) {
                    typeSlotA = UI.Displayed.ALL;
                    typeSlotB = UI.Displayed.DERIVATIVE;
                } else{
                    typeSlotA = UI.Displayed.RAW;
                    typeSlotB = UI.Displayed.DERIVATIVE;
                }

            } else if(y > baseHeight * 2 + 30 && y < baseHeight * 2 + 20 + 100)  {//button 1
                UI.change(typeSlotB);
                if(typeSlotB == UI.Displayed.DERIVATIVE) {
                    typeSlotA = UI.Displayed.RAW;
                    typeSlotB = UI.Displayed.ALL;
                } else{
                    typeSlotA = UI.Displayed.RAW;
                    typeSlotB = UI.Displayed.DERIVATIVE;
                }

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
                        if(UI.getState() == UI.Displayed.ALL || UI.getState() == UI.Displayed.RAW ||
                                UI.getState() == UI.Displayed.DERIVATIVE ){
                            lastState = UI.getState();
                            lastAllState = lastState;
                        } else if(UI.getState() == UI.Displayed.SINGLE) {
                            lastState = UI.getState();
                        }
                        UI.change(UI.Displayed.MENU);
                    }
                }
                UI.update();
    }
}

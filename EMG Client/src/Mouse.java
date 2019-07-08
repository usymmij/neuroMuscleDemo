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
        CommThread.setMode(CommThread.CommThreadMode.IDLE);
    }

    private void hotbarClickedOptions() {
                if (x > UI.getWidth() - 42) {//clicked the x button
                    Exit.normal();
                } else if(x > UI.getWidth() - 132) {// clicked the minimize(-) button
                    UI.minimize();
                } else if(x < 50) {// clicked menu button
                    if (UI.getState() != UI.Displayed.MENU) {
                        lastState = UI.getState();
                        UI.change(UI.Displayed.MENU);
                    } else {
                        UI.change(UI.Displayed.ALL);
                    }
                }
                UI.update();
    }
}

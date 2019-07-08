import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import Pages.*;
import Pages.Menu;

import javax.swing.JFrame;

public class UI{
    private static long lastMillis;
    private static Mouse listener;
    private static int width = 0;
    private static JFrame ui;
    private static Displayed currentlyOn = Displayed.ALL;
    private static EMGPage homePage;

    public enum Displayed {
        ALL, MENU, RAW
    }

    public static Displayed getState() {
        return currentlyOn;
    }

    public static int getWidth() {
        return width;
    }

    public static void start() {
        startListener();
        lastMillis = System.currentTimeMillis();
        homePage = new EMGPage();
        ui = new JFrame();
        ui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// closing frame closes program
        ui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ui.setUndecorated(true);
        ui.setResizable(false);
        ui.getContentPane().setBackground(Color.BLACK);
        ui.setVisible(true);
        width = ui.getWidth();
        ui.addMouseListener(listener);
        ui.getContentPane().add(homePage);
        homePage.newSet(ui.getWidth());
    }

    public static void change(Displayed dis) {
        currentlyOn = dis;
        ui.getContentPane().removeAll();
        switch (currentlyOn) {
            case ALL:
                ui.getContentPane().add(homePage);
                break;
            case MENU:
                ui.getContentPane().add(new Menu());
                break;
        }
        ui.validate();
        ui.repaint();
    }

    public static void update() {
        if(currentlyOn == Displayed.ALL) {
            EMGPage.setData(Serial.getNumBuffer());
        }
        ui.validate();
        ui.repaint();
    }

    public static void minimize() {
        ui.setState(Frame.ICONIFIED);
    }

    private static void startListener() {
        listener = new Mouse();
    }
}

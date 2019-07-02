public class GraphicsThread extends  Thread {

    @Override
    public void run() {
        UI.start();
        while(true) {
            if(UI.getState() == UI.Displayed.ALL) {
                UI.update();
            }
        }
    }
}

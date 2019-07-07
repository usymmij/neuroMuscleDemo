public class GraphicsThread extends  Thread {
    private static long lastTime;
    private static long runTime;
    private static long diff;

    private static void loopContents() {
        diff = System.currentTimeMillis() - lastTime;
        if(diff > 10) {
            UI.update();
            lastTime = System.currentTimeMillis() - (diff + runTime - 10);
            System.out.println(diff);
            try {
                Thread.sleep(9 - runTime);
            } catch(InterruptedException e) {
                Exit.exitWithError("Thread failed to pause");
            }
        }
    }

    @Override
    public void run() {
        UI.start();
        lastTime = System.currentTimeMillis();
        loopContents();
        runTime = System.currentTimeMillis() - lastTime;
        while(true) {
            loopContents();
        }
    }
}

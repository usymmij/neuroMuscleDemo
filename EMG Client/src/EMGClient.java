public class EMGClient {
    private static CommThread commthread;
    private static GraphicsThread graphicsThread;
    public static boolean portSelected = false;

    public static void main(String[] args) {
        try {
            graphicsThread = new GraphicsThread();
            commthread = new CommThread();

            graphicsThread.start();
            commthread.start();

        } catch(Exception e) {
            Exit.exitWithError("threads failed to run");
        }
    }
}

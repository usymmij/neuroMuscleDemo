/**
 * A desktop client that pairs with the Muscle SpikerShield PRO by Backyard brains
 * Load the arduino code provided at
 * https://github.com/usymmij/neuroMuscleDemo
 *
 * usymmij 2019
 */

public class EMGClient {
    private static CommThread commthread;
    private static GraphicsThread graphicsThread;

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

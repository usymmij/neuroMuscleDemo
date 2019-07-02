public class CommThread extends Thread {
    private static Serial serial;
    private static boolean isRunning = true;
    public static CommThreadMode mode = CommThreadMode.IDLE;
    public enum CommThreadMode {
        INPUT, OUTPUT, IDLE
    }

    public static void setMode(CommThreadMode setMode) {
        mode = setMode;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                switch (mode) {
                    case IDLE:
                        Serial.setPort();
                        break;
                    case INPUT:
                        break;
                    case OUTPUT:
                        Serial.EMGRead();
                        break;
                }
            }
        } catch (Exception e) {
            Exit.exitWithError(
                    "fatal communication error: the program will now exit");
        }
    }
}

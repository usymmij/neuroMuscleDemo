import javax.swing.*;

public class Exit {
    public static void normal() {
        System.exit(0);
    }

    public static void exitWithError(String message) {
        JOptionPane.showMessageDialog(null, message,
            "program exited with error", JOptionPane.INFORMATION_MESSAGE);
        System.exit(2);
    }
}

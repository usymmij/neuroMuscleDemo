import com.fazecast.jSerialComm.SerialPort;

public class Serial
{
    private static SerialPort port;
    private static int[] numBuffer = new int[6];
    private static boolean idRead = false;
    private static int currID = 0;
    private static int currValue = 0;

    public static void setPort(){
        try {
            port = SerialPort.getCommPorts()[0];
            System.out.println(SerialPort.getCommPorts()[0]);
            port.openPort();
            CommThread.setMode(CommThread.CommThreadMode.OUTPUT);
        } catch(Exception e) {
        }
    }

    public static int EMGRead() {
        if(port.bytesAvailable() > 0) {
            byte[] buffer = new byte[port.bytesAvailable()];
            port.readBytes(buffer, buffer.length);
            for(int i = 0; i < buffer.length; i++) {
                if((char)buffer[i] == '\n') {
                    System.out.println(currID + ": " + currValue);
                    currValue = 0;
                    idRead = false;
                } else if(!idRead) {
                    currID = buffer[i] - 48;
                    idRead = true;
                } else {
                    if(buffer[i] != 13) {
                        currValue *= 10;
                        currValue += (buffer[i] - 48);
                    }
                }
            }
        }
        return 0;
    }

}

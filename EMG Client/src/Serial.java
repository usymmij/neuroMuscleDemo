import com.fazecast.jSerialComm.SerialPort;

public class Serial
{
    private static SerialPort port;
    private static byte[] numBuffer = {'a', 'a', 'a', 'a', 'a'};

    public static void setPort(){
        try {
            port = SerialPort.getCommPorts()[0];
            port.openPort();
            CommThread.setMode(CommThread.CommThreadMode.OUTPUT);
        } catch(Exception e) {
        }
    }

    public static int EMGRead() {
        if(port.bytesAvailable() > 0) {
            byte[] buffer = new byte[port.bytesAvailable()];
            System.out.println(port.readBytes(buffer, buffer.length));
        }
        return 0;
    }

}

package Pages;

import java.awt.Graphics;

public class EMGPage extends Page {
    private static boolean raw = false;
    private static int[][] smoothData = new int[6][100];
    private static int[][] allData = new int[6][100];
    private static int[] nowData = new int[6];
    private static int sampleLength = 100;//in decieconds
    private final static int Y_BASE_CONSTANT = 170;
    private final static int SPEED_CONTROL = 6;//inversed, higher number is shorter line
    private final static int Y_CONSTANT = 6; //inversed

    public void newSet(int length) {
        smoothData = new int[6][length];
    }

    public static void setData(int[] inData) {
        nowData = inData;
        int length = allData[0].length;
        for(int i = 0; i < 6; i++) {
            for(int j = 1; j < length; j++) {
                allData[i][length - j] = allData[i][length - 1 - j];
            }
            allData[i][0] = nowData[i];
        }
}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int xStep = getWidth() / (sampleLength - 1);

        for(int emgIndex = 0; emgIndex < 6; emgIndex++) {
            int yLine = (1 + emgIndex) * Y_BASE_CONSTANT;
            if(raw) {
                //g.fillRect(x1, y1, xStep, 20);
                for (int i = 0; i < sampleLength - 1; i++) {
                int x1 = i * xStep;
                int x2 = x1 + xStep;
                int y1 = yLine - (allData[emgIndex][i] / Y_CONSTANT);
                int y2 = yLine - (allData[emgIndex][i + 1] / Y_CONSTANT);
                g.drawLine(x1, y1, x2, y2);
                }
            } else {
                lineRounder(g, xStep, yLine, emgIndex);
                //derivative(g, xStep, yLine, emgIndex);
            }
        }
    }

    private static void lineRounder(Graphics g, int xCoef, int yLine, int port) {
        rotateSmoothData(xCoef / SPEED_CONTROL);
        int d2 = allData[port][0] - allData[port][1];
        int d1 = allData[port][1] - allData[port][2];
        int dd = d2 - d1;
        dd /= xCoef;
        smoothData[port][0] = allData[port][1] + dd;
        for(int i = 1; i < xCoef; i++) {
            smoothData[port][i] = smoothData[port][i - 1] + dd;
        }
        for(int i = 0; i < smoothData[0].length - 1; i++) {
            int y1 = yLine - (smoothData[port][i] / Y_CONSTANT);
            int y2 = yLine - (smoothData[port][i + 1] / Y_CONSTANT);
            g.drawLine(i, y1,i + 1,y2);
        }
    }

    private static void derivative(Graphics g, int xCoef, int yLine, int port) {
        rotateSmoothData(xCoef/10);
        int dd = allData[port][0] - allData[port][1];
        dd /= xCoef;
        smoothData[port][0] = dd;
        for(int i = 1; i < xCoef; i++) {
            smoothData[port][i] = smoothData[port][i - 1] + dd;
        }
        for(int i = 0; i < smoothData[0].length - 1; i++) {
            int y1 = yLine - (smoothData[port][i] / Y_CONSTANT);
            int y2 = yLine - (smoothData[port][i + 1] / Y_CONSTANT);
            g.drawLine(i, y1,i + 1,y2);
        }
    }

    private static void rotateSmoothData(int rots) {
        for(int i = 0; i < 6; i++) {
            int length = smoothData[i].length;
            for(int j = 1; j < length - rots + 1 ; j++) {
                smoothData[i][length - j] =  smoothData[i][length - j - rots];
            }
        }
    }
}

package Pages;

import java.awt.Graphics;

public class EMGPage extends Page {
    public static int mode = 0;//0 = normal, 1 = raw, 2 = deriv
    public static int display = 0;//1-6 = EMG, 0 = all
    private static double[] allX;
    private static double[][] smoothData;
    private static int[][] derivData;
    private static int[][] allData = new int[6][100];
    private static int[] nowData = new int[6];
    private static int sampleLength = 100;//in decieconds
    private final static int Y_BASE_CONSTANT = 170;
    private final static int Y_CONSTANT = 6; //inversed

    public void newSet(int length) {
        smoothData = new double[6][length];
        derivData = new int[6][length];
        allX = new double[100];
        for(int i = 0; i < 100; i++) {
            allX[i] = i * (double)(getWidth() / (sampleLength - 1));
        }
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
        //System.out.println(mode + " " + display);
        int xStep = getWidth() / (sampleLength - 1);

        if(display == 0) {
            for (int emgIndex = 0; emgIndex < 6; emgIndex++) {
                paint(g, emgIndex, xStep, 0);
            }
        } else {
            for(int i = 0; i < 3; i++) {
                mode = i;
                paint(g, display + 1, xStep, i + 1);
            }
        }
    }

    private static void paint(Graphics g,int emgIndex, int xStep, int singleModeLinePush) {
        int yLine = 0;
        if(singleModeLinePush == 0) {
            yLine = (1 + emgIndex) * Y_BASE_CONSTANT;
        } else {
            yLine = (((singleModeLinePush - 1) * 2) + 1) * Y_BASE_CONSTANT;
        }
        if(mode == 0) {
            lineRounder(g, xStep, yLine, emgIndex);
        } else if(mode == 1){
            //g.fillRect(x1, y1, xStep, 20);
            for (int i = 0; i < sampleLength - 1; i++) {
                int x1 = i * xStep;
                int x2 = x1 + xStep;
                int y1 = yLine - (allData[emgIndex][i] / Y_CONSTANT);
                int y2 = yLine - (allData[emgIndex][i + 1] / Y_CONSTANT);
                g.drawLine(x1, y1, x2, y2);
            }
        } else if(mode == 2) {
            derivative(g, xStep, yLine, emgIndex);
        }
    }

    private static void lineRounder(Graphics g, int xCoef, int yLine, int port) {
        int d1 = allData[port][0] - allData[port][1];
        int d2 = allData[port][1] - allData[port][2];
        int dd = (d1 - d2) / (xCoef);
        int currSlope = d2 / (xCoef);
        smoothData[port][0] = allData[port][0];
        for(int i = 1; i < xCoef; i++) {
            smoothData[port][i] = smoothData[port][i - 1] + (currSlope / xCoef);
            currSlope += (dd/xCoef);
        }
        for(int i = 0; i < smoothData[0].length - 1; i++) {
            int y1 = (int)(yLine - (smoothData[port][i] / Y_CONSTANT));
            int y2 = (int)(yLine - (smoothData[port][i + 1] / Y_CONSTANT));
            g.drawLine(i, y1,i + 1,y2);
            //g.drawLine(i, y1, i, y1);
        }
        rotateSmoothData(xCoef / 10);
    }

    private static void derivative(Graphics g, int xCoef, int yLine, int port) {
        int dd = ((int)allData[port][0]) - (int)allData[port][1];
        dd /= xCoef;
        derivData[port][0] = dd;
        for(int i = 1; i < xCoef; i++) {
            derivData[port][i] = derivData[port][i - 1] + dd;
        }
        for(int i = 0; i < derivData[0].length - 1; i++) {
            int y1 = yLine - (derivData[port][i] / Y_CONSTANT);
            int y2 = yLine - (derivData[port][i + 1] / Y_CONSTANT);
            g.drawLine(i, y1,i + 1,y2);
        }
        rotateDerivData(xCoef/10);
    }

    private static void rotateSmoothData(int rots) {
        for(int i = 0; i < 6; i++) {
            int length = smoothData[i].length;
            for(int j = 1; j < length - rots + 1 ; j++) {
                smoothData[i][length - j] = smoothData[i][length - j - rots];
            }
        }
    }

    private static void rotateDerivData(int rots) {
        for(int i = 0; i < 6; i++) {
            int length = derivData[i].length;
            for(int j = 1; j < length - rots + 1 ; j++) {
                derivData[i][length - j] =  derivData[i][length - j - rots];
            }
        }
    }
}

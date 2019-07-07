package Pages;

import java.awt.Graphics;

public class EMGPage extends Page {
    private static boolean raw = true;
    private static int[][] allData = new int[6][100];
    private static int[] nowData = new int[6];
    private static int sampleLength = 100;//in decieconds
    private final static int Y_BASE_CONSTANT = 170;
    private final static int Y_CONSTANT = 6; //inversed, higher number is shorter line

    public EMGPage() {
        super();
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
            for (int i = 0; i < sampleLength - 1; i++) {
                int x1 = i * xStep;
                int x2 = x1 + xStep;
                int y1 = ((1 + emgIndex) * Y_BASE_CONSTANT) - (allData[emgIndex][i] / Y_CONSTANT);
                int y2 = ((1 + emgIndex) * Y_BASE_CONSTANT) - (allData[emgIndex][i + 1] / Y_CONSTANT);
                if(raw) {
                    g.drawLine(x1, y1, x2, y2);
                } else {
                    lineRounder(g, x1, y1, x2, y2);
                }
            }
        }
    }

    private static void lineRounder(Graphics g, int x1, int y1, int x2, int y2) {
        //splineInterpolator();
    }
}

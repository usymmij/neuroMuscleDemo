package Pages;

import java.awt.Graphics;

public class EMGPage extends Page {
    private static boolean displaysAll = true;
    private static int[][] allData = new int[6][500];
    private static int[] nowData = new int[6];
    private static int sampleLength = 1000;//in milliseconds
    private final static int Y_BASE_CONSTANT = 170;
    private final static int Y_CONSTANT = 3; //inversed, higher number is shorter line

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
        int xStep = getWidth() / sampleLength;

        for(int emgIndex = 0; emgIndex < 6; emgIndex++) {
            for (int i = 0; i < sampleLength - 1; i++) {
                int x1 = getWidth() - (i * xStep);
                int x2 = x1 - xStep;
                int y1 = ((1 + emgIndex) * Y_BASE_CONSTANT) - (allData[emgIndex][i] / Y_CONSTANT);
                int y2 = ((1 + emgIndex) * Y_BASE_CONSTANT) - (allData[emgIndex][i + 1] / Y_CONSTANT);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}

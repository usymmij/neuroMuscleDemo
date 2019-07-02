package Pages;

import java.awt.Graphics;

public class EMGPage extends Page {
    private static boolean displaysAll = true;
    private static int[][] allData = new int[6][100];
    private static int[] nowData = new int[6];
    private static int sampleLength = 100;//in deciseconds
    private final static int Y_CONSTANT = 170;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int xStep = getWidth() / sampleLength;

        for(int emgIndex = 0; emgIndex < 6; emgIndex++) {
            for (int i = 0; i < sampleLength - 1; i++) {
                int x1 = getWidth() - (i * xStep);
                int x2 = x1 - xStep;
                int y1 = ((1 + emgIndex) * Y_CONSTANT) - allData[emgIndex][i];
                int y2 = ((1 + emgIndex) * Y_CONSTANT) - allData[emgIndex][i + 1];
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}

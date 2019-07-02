public class EMGs {
    private static int[] EMGData = new int[6];
    private static int[][] pastData = new int[6][100];
    private static long lastMillis = 0;
    private static long setDiff = 100;

    public void setEMGData(int data, int index) {
        EMGData[index] = data;
    }

    public int[][] getAllData() {
        return pastData;
    }

    private void rotatePastData(int i) {
        for(int j = 0; j < 99; j++) {
            pastData[i][j + 1] = pastData[i][j];
        }
    }

    public void updateData() {
        long difference = System.currentTimeMillis() - lastMillis;
        if(difference > setDiff) {
            for(int i = 0; i < 6; i++) {
                rotatePastData(i);
                pastData[i][0] = EMGData[i];
                lastMillis += 100;
            }
            if(difference > 110) {
                System.out.println("loop overrun");//debugging to check for speed
                setDiff -= 1;
            } else if(difference < 90) {
                System.out.println("loop underrun");
                setDiff += 1;
            }
        }
    }
}
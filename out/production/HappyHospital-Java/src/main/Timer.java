package main;

public class Timer {

    public double secondsSinceStart;
    private GamePanel gp;

    public static final int DURATION = 4;

    public Timer(GamePanel gp) {
        this.gp = gp;
        secondsSinceStart = 0;
    }

    public void update() {
        secondsSinceStart += (double) 1/gp.FPS;
    }

    public static String getFormattedTime(double second) {
        StringBuilder stringBuilder = new StringBuilder();

        int seconds = (int) (second % 60);
        int minutes = (int) (second / 60);
        int hours = (int) (second / 60 / 60);

        if(hours < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hours);
        stringBuilder.append(":");

        if(minutes < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(minutes);
        stringBuilder.append(":");

        if(seconds < 10) {
            stringBuilder.append(0);
        }
        stringBuilder.append(seconds);
        return stringBuilder.toString();
    }

}

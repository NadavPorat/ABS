package time;

public interface ITimeUnit {

    boolean moveTimeForward(int timeToAdd);

    boolean moveTimeBeck(int timeToReduce);

    boolean initToYaz1();

    int getCurrentTime();
}

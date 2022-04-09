package time;

//singleton class
public class TimeUnitABS implements ITimeUnit {
    private static TimeUnitABS single_instance = null;
    private int currTime;

    private TimeUnitABS() {
        currTime = 1;
    }

    public static ITimeUnit TimeUnitABS() {
        // To ensure only one instance is created
        if (single_instance == null) {
            single_instance = new TimeUnitABS();
        }
        return single_instance;
    }

    @Override
    public boolean moveTimeForward(int timeToAdd) {
        boolean isSet = false;
        if (timeToAdd > 0) {
            single_instance.currTime = single_instance.currTime + timeToAdd;
            isSet = true;

        }
        return isSet;
    }

    @Override
    public boolean moveTimeBeck(int timeToReduce) {
        boolean isSet = false;
        if (timeToReduce < 0) {
            single_instance.currTime = single_instance.currTime - timeToReduce;
            isSet = true;
        }
        return isSet;
    }

    @Override
    public boolean initToYaz1() {
        single_instance.currTime = 1;
        return true;
    }

    @Override
    public int getCurrentTime() {
        return single_instance.currTime;
    }
}

package engine.utils;

public class TimeUnitABS implements ITimeUnit {
    private static int currTime;


    public TimeUnitABS() {
        currTime = 0;
    }


    public static boolean setTime(int timeTOAdd){
        boolean isSet = false;
        if (timeTOAdd > 0) {
            currTime = currTime + timeTOAdd;
            isSet = true;
        }
        return isSet;
    }


    public static int getCurrentTime() {
       return currTime;
    }
}

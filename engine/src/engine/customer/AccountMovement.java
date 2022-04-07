package engine.customer;

import engine.utils.ITimeUnit;

public class AccountMovement {

    private final Integer actionID;
    private final int actionYaz;
    private final eActionType actionType;
    private final Integer statusAccountBefore;
    private final Integer statusAccountAfter;



    public AccountMovement(Integer id, int yaz, eActionType actionType, Integer statusAccountBefore, Integer statusAccountAfter)
    {
        this.actionID = id;
        this.actionType = actionType;
        this.actionYaz = yaz;
        this.statusAccountBefore = statusAccountBefore;
        this.statusAccountAfter = statusAccountAfter;

    }

    @Override
    public String toString() {
        return "AccountMovement{" +
                "actionID=" + actionID +
                ", actionYaz=" + actionYaz +
                ", actionType=" + actionType +
                ", statusAccountBefore=" + statusAccountBefore +
                ", statusAccountAfter=" + statusAccountAfter +
                '}';
    }
}

package customer;

public class AccountMovement {

    private final Integer actionID;
    private final int actionYaz;
    private final eActionType actionType;
    private final Integer statusAccountBefore;
    private final Integer statusAccountAfter;
    private final Integer moneyAmount;



    public AccountMovement(Integer id, int yaz, eActionType actionType, Integer statusAccountBefore, Integer statusAccountAfter, Integer moneyAmount)
    {
        this.actionID = id;
        this.actionType = actionType;
        this.actionYaz = yaz;
        this.statusAccountBefore = statusAccountBefore;
        this.statusAccountAfter = statusAccountAfter;
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "AccountMovement{" +
                ", actionYaz=" + actionYaz +
                ", moneyAmount=" + moneyAmount +
                ", actionType=" + actionType +
                ", statusAccountBefore=" + statusAccountBefore +
                ", statusAccountAfter=" + statusAccountAfter +
                '}';
    }
}

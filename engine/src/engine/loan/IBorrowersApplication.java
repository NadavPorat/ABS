package engine.loan;

import engine.utils.ITimeUnit;

public interface IBorrowersApplication {
    Integer applicationID = null; //hash code? uniq
    String costumerName = null;
    Long loanAmount = null;
    ITimeUnit loanTimeRange = null;
    String returnRate = null;
    String insertRate = null;
    String category = null;
}

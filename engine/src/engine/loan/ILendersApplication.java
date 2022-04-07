package engine.loan;

public interface ILendersApplication {

    Integer amountMoneyToLoan = null; // (req)
    String category = null;
    String minInterest= null;
    Integer minTUToLoan= null;
    Long maxOwnerOnLoan= null;
    Integer maxAmountLoanOpen= null;

}

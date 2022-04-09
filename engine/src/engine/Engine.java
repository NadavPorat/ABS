package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Engine {

    boolean Withdrawal(String customerID, int amount);
    boolean Deposit(String customerID, int amount);
    ArrayList<String> filterLoans(String customerID, int amount, String category, int interestParYaz, int minYazForLoan);
    boolean buildFromXML(String path);
    StringBuilder printExistLoans();
    StringBuilder printCustomers();
    boolean isInitialized();
    int getCurrentTime();
    ArrayList<String> getCustomersNames();
    Map<String,String> getCustomerAndBalanceList();
    List<String> getListOfCategory();
    boolean placementLoanByName(String customerID, int amount, ArrayList<String> loanNameToInvest) throws Exception;
}

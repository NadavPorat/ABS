package customer;

import loan.BankLoan;
import loan.Loan;

import java.util.ArrayList;
import java.util.Map;

public interface ICustomer {
    Integer getBalance();

    boolean withdrawal(int amount);

    boolean deposit(int amount);

    ArrayList<AccountMovement> getAccountMovements();

    String getCustomerName();

    Map<String, Loan> getMyLoans();

    Map<String, Loan> getInvestedLoans();
}

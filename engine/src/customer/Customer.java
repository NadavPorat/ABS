package customer;

import engine.ABSEngine;
import loan.BankLoan;
import loan.Loan;
import time.ITimeUnit;
import time.TimeUnitABS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Customer implements ICustomer {

    private final ITimeUnit currYaz = TimeUnitABS.TimeUnitABS();
    private String customerName;
    //private Integer customerID;
    private Integer balance;
    private ArrayList<AccountMovement> accountMovements;

    private Map<String, Loan> myLoans; //key is name
    private Map<String, Loan> InvestedLoans; // key is name

    public Customer(String customerName, Integer balance) {
        if (balance >= 0) {
            this.balance = balance;
            this.customerName = customerName;
        } else {
            throw new IllegalArgumentException("Customer cant be with negative balance");
        }
        accountMovements = new ArrayList<>();
        myLoans = new HashMap<>();
        InvestedLoans = new HashMap<>();
    }

    @Override
    public Integer getBalance() {
        return balance;
    }

    private void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public boolean withdrawal(int amount) {
        boolean isWithdrawal;
        int balance = this.getBalance();
        int expectedBalance = balance - amount;

        if (amount < 0) {
            throw new IllegalArgumentException("Customer Can't Withdrawal Negative Amount, Please Try Again");
        } else if (expectedBalance < 0) {
            throw new IllegalArgumentException("Customer Can't Be With Negative Balance, Your Max Withdrawal Can be:  " + balance);
        } else {
            this.setBalance(expectedBalance);
            int movementID = accountMovements.size() + 1;
            AccountMovement movement = new AccountMovement(movementID, currYaz.getCurrentTime(), eActionType.WITHDRAWAL, balance, expectedBalance, amount);
            accountMovements.add(movement);
            isWithdrawal = true;
        }
        return isWithdrawal;
    }

    @Override
    public boolean deposit(int amount) {
        boolean isDeposit ;
        int balance = this.getBalance();
        int expectedBalance = balance + amount;
        if (amount < 0) {
            throw new IllegalArgumentException("Customer Can't Deposit Negative Amount, Please Try Again");
        } else {
            this.setBalance(expectedBalance);
            int movementID = accountMovements.size() + 1;
            AccountMovement movement = new AccountMovement(movementID, currYaz.getCurrentTime(), eActionType.DEPOSIT, balance, expectedBalance, amount);
            accountMovements.add(movement);
            isDeposit = true;
        }
        return isDeposit;
    }

    @Override
    public ArrayList<AccountMovement> getAccountMovements() {
        return accountMovements;
    }

    @Override
    public String getCustomerName() {
        return this.customerName;
    }

    private StringBuilder printAccountMovements() {
        int index = 1;
        StringBuilder outMessage = new StringBuilder();
        for (AccountMovement movement : accountMovements) {
            outMessage.append(" ");
            outMessage.append(index);
            outMessage.append(". ");
            outMessage.append(movement.toString());
            outMessage.append('\n');
            index++;
        }
        return outMessage;

    }

    @Override
    public String toString() {
        String movementsToString;
        String myLoansToString ;
        String InvestedLoansToString;
        int index = 1;
        if (accountMovements != null) {
            movementsToString = printAccountMovements().toString();
        } else {
            movementsToString = " There is No Movements Recorded";
        }
        if (myLoans != null) {
            StringBuilder outM = new StringBuilder("myLoansInfo{ ");
            for (Loan loan : myLoans.values()) {
                outM.append(index).append(".").append(loan.toString()).append('\'');
                outM.append(ABSEngine.provideInfoLoanStatusToCustomer(loan.getId()));
                outM.append('}');
                index++;
            }
            outM.append("}");
            myLoansToString = outM.toString();

        } else {
            myLoansToString = " There is No Loans Till Now";
        }

        if (InvestedLoans != null) {
            index = 1;
            StringBuilder outM = new StringBuilder("InvestedLoansInfo{ ");
            for (Loan loan : InvestedLoans.values()) {
                outM.append(index).append(".").append(loan.toString()).append('\'');
                index++;
            }
            outM.append("}");
            InvestedLoansToString = outM.toString();
        } else {
            InvestedLoansToString = " There is No Invested Loans Till Now";
        }


        return "Customer{" +
                "customerName='" + customerName + '\'' +
                ", balance=" + balance +
                ", accountMovements=" + movementsToString +
                ", myLoans=" + myLoansToString +
                ", InvestedLoans=" + InvestedLoansToString +
                '}';
    }

    @Override
    public Map<String, Loan> getMyLoans() {
        return myLoans;
    }
    @Override
    public Map<String, Loan> getInvestedLoans() {
        return InvestedLoans;
    }
}

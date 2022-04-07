package engine.customer;

import engine.utils.ITimeUnit;

import java.util.ArrayList;

import static engine.utils.ITimeUnit.getCurrentTime;

public class Customer implements ICustomer {


    private String customerName;
    private Integer customerID;
    private Integer balance;
    private ArrayList<AccountMovement> accountMovements;
    private ArrayList<Long> myLoans;
    private ArrayList<Long> InvestedLoans;

    public Customer(String customerName, Integer balance) {


        if (balance >= 0) {
            this.balance = balance;
            this.customerName = customerName;
        } else {
            //TODO exeptions for customer
            throw new IllegalArgumentException("Customer cant be with negative balance");
        }
    }

    public Integer getBalance() {
        return balance;
    }

    private void setBalance(Integer balance) {
        this.balance = balance;
    }

    protected boolean withdrawal(int amount) {
        boolean isWithdrawal = false;
        int balance = this.getBalance();
        int expectedBalance = balance - amount;
        if (expectedBalance < 0) {
            //TODO exeptions for customer
            throw new IllegalArgumentException("Customer cant be with negative balance");
        } else {
            this.setBalance(expectedBalance);
            int movementID = accountMovements.size();
            int yaz = getCurrentTime();
            AccountMovement movement = new AccountMovement(movementID,yaz, eActionType.WITHDRAWAL, balance, expectedBalance);
            accountMovements.add(movement);
            isWithdrawal = true;
        }
        return isWithdrawal;
    }


    protected boolean deposit(int amount) {
        boolean isDeposit = false;
        int balance = this.getBalance();
        int expectedBalance = balance + amount;
        if (expectedBalance < 0) {
            //TODO exeptions for customer
            throw new IllegalArgumentException("Customer cant be with negative balance");
        } else {
            this.setBalance(expectedBalance);
            int movementID = accountMovements.size();
            int yaz = getCurrentTime();
            AccountMovement movement = new AccountMovement(movementID,yaz, eActionType.DEPOSIT, balance, expectedBalance);
            accountMovements.add(movement);
            isDeposit = true;
        }
        return isDeposit;
    }

    public ArrayList<AccountMovement> getAccountMovements() {
        return accountMovements;
    }

    @Override
    public Integer getCustomerID() {
        return this.customerID;
    }

    private void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    @Override
    public String getCustomerName() {
        return this.customerName;
    }

    private void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

package loan;

import customer.Customer;
import time.ITimeUnit;
import time.TimeUnitABS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankLoan extends Loan  {
    private final ITimeUnit currYaz = TimeUnitABS.TimeUnitABS();


    //info time loan
    private int yazActivated;
    private int lastYazPaid;
    private int closeYaz;

    //dynamic
    private int currentFinancingForLoan;
    private int timeLeftTOLoan;

    private Map<Customer, Integer> lenders;
    private ArrayList<Payment> payments;
    private ArrayList<Payment> unPaidPayments;

    ///customer side
    private int paidInterest;
    private int remainingInterest;
    private int paidFund;

    public BankLoan(String owner, String category, int capital, int totalYazTime, int paysEveryYaz, int interestPerPayment, String id) {
        super(owner, category, capital, totalYazTime, paysEveryYaz, interestPerPayment, id);
        lenders= new HashMap<>();
        payments = new ArrayList<>();
        unPaidPayments = new ArrayList<>();
    }

    public void addToPaidFund(int paidFund) throws Exception {
        int expectedPaidFund = this.currentFinancingForLoan += paidFund;
        if(expectedPaidFund > this.capital)
        {
            throw new IllegalArgumentException("With This Amount We Cross The Fund Required");
        }
        else if (expectedPaidFund == capital)
        {
            this.currentFinancingForLoan = expectedPaidFund;
            this.yazActivated = currYaz.getCurrentTime();
            this.status = LoanStatus.Active;
        }
        else
        {
            this.status = LoanStatus.Pending;
            this.currentFinancingForLoan = expectedPaidFund;
        }


    }




    public Map<Customer, Integer> getLenders() {
        return lenders;
    }

    public int getPaidFund() {
        return paidFund;
    }



    public int getYazActivated() {
        return yazActivated;
    }

    public int getCloseYaz() {
        return closeYaz;
    }

    public int getLastYazPaid() {
        return lastYazPaid;
    }

    private String printLenders() {
        if (lenders != null ) {
            String out = "{Lenders List is:";
            int index = 1;
            out += "{";
            for (Customer keys : lenders.keySet()) {
                out += index + "." + keys.getCustomerName() +" Invested With= " +lenders.get(keys) + ' ';
                index++;

            }
            out += "}";

            return out;
        } else {
            return "No Lenders";
        }

    }

    private boolean addMoneyToFinancing(int moneyAmount) {
        boolean isAddMoney =false;
       if(moneyAmount>0) {
           this.currentFinancingForLoan += moneyAmount;
           isAddMoney = true;
           if (remainedMoneyToPending() == 0) {
                //TODO move from panding to active
           }
       }

       return isAddMoney;

    }

    public int remainedMoneyToPending() {
        return this.capital - this.currentFinancingForLoan;
    }

    public int getNextYazPayment() {
        int nextYazToPay;
        int leftTillPay = paysEveryYaz - (currYaz.getCurrentTime() - lastYazPaid);
        nextYazToPay = currYaz.getCurrentTime() + leftTillPay;
        return nextYazToPay;
    }


    //TODO add when pay wasn't pay
    private String InRiskLoanInfoToPrint() {
        String out = activeLoanInfoToPrint();

        return out;
    }

    private String FinishedLoanInfoToPrint() {
        String out =  this.status +", ";
        out += pendingLoanInfoToPrint();
        out = out + '\'' + "Start Loan yaz =' " + yazActivated + '\'' +
                "End Loan yaz = '" + lastYazPaid + '\'' +
                getPaymentInfo() + '\'';
        return out;
    }

    private String pendingLoanInfoToPrint() {
        return this.status +", "+ printLenders() + '\'' +
                ", amountMoneyCollect= " + this.currentFinancingForLoan +
                ", Money left to pay= " + remainedMoneyToPending()+ "}";
    }

    private String activeLoanInfoToPrint() {
        return this.status +", "+ printLenders() + '\'' +
                ", amountMoneyCollect= ' " + this.currentFinancingForLoan + '\'' +
                ", yaz Loan activated= '" + yazActivated + '\'' +
                ",yaz next pay = '" + getNextYazPayment() + '\'' +
                ", payment information =" + getPaymentInfo() + '\'' +
                ", Sum Fund Paid = " + paidFund + '\'' +
                ", Sum interest Paid = " + paidInterest + '\'' +
                ", Sum Fund Remained To Pay = " + remainedMoneyToPending() + '\'' +
                ", Sum interest Remained To Pay = " + remainingInterest + '\'';
    }
    @Override
    public String toString() {
        String toStringStatus = null;
        switch (this.status) {
            case New:
                toStringStatus = "This is New Loan";
                break;
            case Active:
                toStringStatus = activeLoanInfoToPrint();
                break;
            case Pending:
                toStringStatus = pendingLoanInfoToPrint();
                break;
            case Finished:
                toStringStatus = FinishedLoanInfoToPrint();
                break;
            case InRisk:
                toStringStatus = InRiskLoanInfoToPrint();
                break;

            default:
                toStringStatus = "";
        }

        return "Loan{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", category='" + category + '\'' +
                ", capital=" + capital + '\'' +
                ", totalYazTime=" + totalYazTime + '\'' +
                ", paysEveryYaz=" + paysEveryYaz + '\'' +
                ", interestPerPayment=" + interestPerPayment + '\'' +
                ", Loan Status= " + toStringStatus + '\'' +
                '}';
    }

    private String getPaymentInfo() {
        int index = 1;
        if (payments!= null) {
            StringBuilder outM = new StringBuilder("PaymentInfo{ ");
            for (BankLoan.Payment pay : payments) {
                outM.append(index).append(".").append(pay.toString()).append('\'');
                index++;
            }
            outM.append("}");
            return outM.toString();
        } else {
            return "PaymentInfo : No Pay till Now";
        }
    }

    private String provideInfoLoanStatusToCustomer() {
        StringBuilder outM = new StringBuilder("Loan Status is : ");
        outM.append(getLoanStatus());
        switch (getLoanStatus()) {
            case New:
                outM.append("This is New Loan");
                break;
            case Active:
                outM.append("Next yaz to pay is= ");
                outM.append(getNextYazPayment());
                outM.append("Pay Amount(fund+interest)= ");
                outM.append(getNextPaymentAmount());
                break;
            case Pending:
                outM.append("Remained amount of money to collect= ");
                outM.append(remainedMoneyToPending());
            case Finished:
                outM.append("yez Start= ");
                outM.append(getYazActivated());
                outM.append("yez End=");
                outM.append(getCloseYaz());
                break;
            case InRisk:
                outM.append("Number of UnPayments= ");
                outM.append(getNumUnPayments());
                outM.append("In Total Worth= ");
                outM.append(getNumUnPaymentsWorth());
                break;

        }
        return outM.toString();
    }

    public int getNumUnPayments() {
        return unPaidPayments.size();
    }

    public int getNumUnPaymentsWorth() {
        int totalNoPaidMoney = 0;
        for (Payment pay : this.unPaidPayments)
        {
            totalNoPaidMoney+= pay.totalPaid;
        }
        return totalNoPaidMoney;
    }

    private class Payment {
        private int payYaz;
        private int fundCut;
        private int interestCut;
        private int totalPaid;

        public Payment(int payYaz, int fundCut, int interestCut) {
            this.payYaz = payYaz;
            this.fundCut = fundCut;
            this.interestCut = interestCut;
            totalPaid = fundCut + interestCut;
        }
    }


    //TODO from new to pending


    //Todo from pending to active

    //todo moveToRisk


    //todo add to


}

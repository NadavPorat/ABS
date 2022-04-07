package engine.loan;

import engine.customer.Customer;
import engine.utils.TimeUnitABS;

import java.util.List;
import java.util.Map;

public class BankLoan extends Loan {

    //info time loan
    private int yazActivated;
    private int lastYazPaid;

    //dynamic
    private int currentFinancingForLoan;
    private int timeLeftTOLoan;
    private Map<Customer, Integer> lenders;
    private List<Payment> payments;

    ///customer side
    private int paidInterest;
    private int remainingInterest;
    private int paidFund;
    private int remainingFund;


    public BankLoan(String owner, String category, int capital, int totalYazTime, int paysEveryYaz, int interestPerPayment, String id) {
        super(owner, category, capital, totalYazTime, paysEveryYaz, interestPerPayment, id);
    }


    private String printLenders() {
        if (!lenders.isEmpty()) {
            String out = "Lenders List is:";
            int index = 1;
            for (Customer keys : lenders.keySet()) {
                out += "{" + index + "." + keys.getCustomerName() + lenders.get(keys) + '\'';
                index++;
            }
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

    private int remainedMoneyToPending() {
        return this.capital - this.currentFinancingForLoan;
    }

    private int getNextYazPayment() {
        int nextYazToPay;
        int leftTillPay = paysEveryYaz - (TimeUnitABS.getCurrentTime() - lastYazPaid);
        nextYazToPay = TimeUnitABS.getCurrentTime() + leftTillPay;
        return nextYazToPay;
    }

    //TODO add when pay wasn't pay
    private String InRiskLoanInfoToPrint() {
        String out = activeLoanInfoToPrint();

        return out;
    }

    private String FinishedLoanInfoToPrint() {
        String out = pendingLoanInfoToPrint();
        out = out + '\'' + "Start Loan yaz =' " + yazActivated + '\'' +
                "End Loan yaz = '" + lastYazPaid + '\'' +
                getPaymentInfo() + '\'';
        return out;
    }

    private String pendingLoanInfoToPrint() {
        return printLenders() + '\'' +
                ", amountMoneyCollect= ' " + this.currentFinancingForLoan + '\'' +
                ", Money left to pay= '" + remainedMoneyToPending() + '\'';
    }

    private String activeLoanInfoToPrint() {
        return printLenders() + '\'' +
                ", amountMoneyCollect= ' " + this.currentFinancingForLoan + '\'' +
                ", yaz Loan activated= '" + yazActivated + '\'' +
                ",yaz next pay = '" + getNextYazPayment() + '\'' +
                ", payment information =" + getPaymentInfo() + '\'' +
                ", Sum Fund Paid = " + paidFund + '\'' +
                ", Sum interest Paid = " + paidInterest + '\'' +
                ", Sum Fund Remained To Pay = " + remainingFund + '\'' +
                ", Sum interest Remained To Pay = " + remainingInterest + '\'';
    }

    @Override
    public String toString() {
        String toStringStatus = null;
        switch (this.status) {
            case Active:
                toStringStatus = activeLoanInfoToPrint();
                break;
            case Pending:
                toStringStatus = pendingLoanInfoToPrint();
            case Finished:
                toStringStatus = FinishedLoanInfoToPrint();
                break;
            case InRisk:
                toStringStatus = InRiskLoanInfoToPrint();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + this.status);
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
        if (!payments.isEmpty()) {
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

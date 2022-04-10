package loan;

import customer.Customer;
import time.ITimeUnit;
import time.TimeUnitABS;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BankLoan extends Loan {
    private final ITimeUnit yaz = TimeUnitABS.TimeUnitABS();

    //info time loan
    private int yazActivated;
    private int lastYazPaid;
    private int closeYaz;

    //dynamic
    private int currentFinancingForLoan;
    private int timeLeftTOLoan;
    private Map<Customer, Integer> lenders;
    private ArrayList<Payment> payments;

    ///customer side
    private int paidInterest;
    private int remainingInterest;
    private int remainingFund;
    private int paidFund;
    private int sumExpectedPayForLoan;
    private int ExpectedTotalInterest;


    public BankLoan(String owner, String category, int capital, int totalYazTime, int paysEveryYaz, int interestPerPayment, String id) {
        super(owner, category, capital, totalYazTime, paysEveryYaz, interestPerPayment, id);
        lenders = new HashMap<>();
        payments = new ArrayList<>();
        setPayments();
        sumExpectedPayForLoan = 0;
        ExpectedTotalInterest= calcTotalInterestExp();
        timeLeftTOLoan = yaz.getCurrentTime() + totalYazTime;
    }

    public void setTimeLeftTOLoan() {
        this.timeLeftTOLoan = timeLeftTOLoan - 1;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public int getTotalExpectedPayForLoan() {

        return sumExpectedPayForLoan;
    }

    public int getTotalPayForNextPayment() {
        int amountNextPay= -1;
        for (Payment pay : payments) {
            if(!pay.isPaid)
            {
                amountNextPay= pay.totalPay;
                break;
            }
        }
        return  amountNextPay;
    }

    public int getMoneyToPay() {
        for (Payment pay : payments) {
            if (pay.payYaz == yaz.getCurrentTime()) {
                return pay.totalPay;
            }
        }
        return -1; //if is now payment day sign with -1
    }


    public void addToPaidFund(int paidFund) {
        int expectedPaidFund = this.currentFinancingForLoan += paidFund;
        if (expectedPaidFund > this.capital) {
            throw new IllegalArgumentException("With This Amount We Cross The Fund Required");
        } else if (expectedPaidFund == capital) {
            this.currentFinancingForLoan = expectedPaidFund;
            this.yazActivated = yaz.getCurrentTime();
            this.status = LoanStatus.Active;
        } else {
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
        if (lenders != null) {
            String out = "{Lenders List is:";
            int index = 1;
            out += "{";
            for (Customer keys : lenders.keySet()) {
                out += index + "." + keys.getCustomerName() + " Invested With= " + lenders.get(keys) + ' ';
                index++;

            }
            out += "}";

            return out;
        } else {
            return "No Lenders";
        }

    }

    private boolean addMoneyToFinancing(int moneyAmount) {
        boolean isAddMoney = false;
        if (moneyAmount > 0) {
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
        int leftTillPay = paysEveryYaz - (yaz.getCurrentTime() - lastYazPaid);
        nextYazToPay = yaz.getCurrentTime() + leftTillPay;
        return nextYazToPay;
    }


    private String InRiskLoanInfoToPrint() {
        String out = activeLoanInfoToPrint();
        out += getUnPaid();
        return out;
    }

    public String getNumUnPayments() {
        ArrayList<Payment> unPayments = (ArrayList<Payment>) payments.stream().filter(p -> !p.shouldBePaid).collect(Collectors.toList());
        int numUnPaid = unPayments.size();
        return String.valueOf(numUnPaid);
    }

    public String getUnPaymentsWorth() {
        int totalUnPay = 0;
        ArrayList<Payment> unPayments = (ArrayList<Payment>) payments.stream().filter(p -> !p.shouldBePaid).collect(Collectors.toList());
        for (Payment pay : unPayments) {
            totalUnPay += pay.totalPay;
        }
        return String.valueOf(totalUnPay);
    }

    private String getUnPaid() {
        StringBuilder out = new StringBuilder();
        int totalUnPay = 0;
        int countUnPaid = 0;
        ArrayList<Payment> unPayments = (ArrayList<Payment>) payments.stream().filter(p -> !p.shouldBePaid).collect(Collectors.toList());
        out.append("The Next Payments Should be Paid But didn't { ");
        for (Payment pay : unPayments) {
            totalUnPay += pay.totalPay;
            countUnPaid++;
            out.append(pay.toString());
        }

        out.append(" }");
        out.append("Number Of UnPaid Payments is = " + countUnPaid);
        out.append(", Total Sum Of The UnPaid Payments is = " + totalUnPay);
        return out.toString();
    }

    private String FinishedLoanInfoToPrint() {
        String out = this.status + ", ";
        out += pendingLoanInfoToPrint();
        out = out + '\'' + "Start Loan yaz =' " + yazActivated + '\'' +
                "End Loan yaz = '" + lastYazPaid + '\'' +
                getPaymentInfo() + '\'';
        return out;
    }

    private String pendingLoanInfoToPrint() {
        return this.status + ", " + printLenders() + '\'' +
                ", amountMoneyCollect= " + this.currentFinancingForLoan +
                ", Money left to pay= " + remainedMoneyToPending() + "}";
    }

    private String activeLoanInfoToPrint() {
        return this.status + ", " + printLenders() + '\'' +
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
        if (payments != null) {
            StringBuilder outM = new StringBuilder("PaymentInfo{ ");
            for (Payment pay : payments) {
                if (pay.isPaid()) {
                    outM.append(index).append(".").append(pay.toString()).append('\'');
                    index++;
                }
            }
            outM.append("}");
            return outM.toString();
        } else {
            return "PaymentInfo : No Pay till Now";
        }
    }


    public void setPayments() {
        int totalPayByTheCustomer = 0;
        int AmountOfPayments = this.totalYazTime / this.paysEveryYaz;
        int currYaz = this.yaz.getCurrentTime();
        int payYaz = currYaz + paysEveryYaz - 1;
        for (int i = 0; i < AmountOfPayments; i++) {
            Payment pay = calcPayment();
            pay.setPayYaz(payYaz);
            totalPayByTheCustomer += pay.totalPay;
            payYaz = payYaz + paysEveryYaz;
            payments.add(pay);
        }
        this.sumExpectedPayForLoan = totalPayByTheCustomer;

    }

    public Payment calcPayment() {
        DecimalFormat df = new DecimalFormat("0.00");

        int AmountOfPayments = this.totalYazTime / this.paysEveryYaz;
        double foundParPayment = this.capital / AmountOfPayments;
        double interestParsToDouble = Double.parseDouble(df.format(this.interestPerPayment));
        double interestPercentage = (interestParsToDouble / 100);
        double interestToPay = foundParPayment * interestPercentage;
        return new Payment((int) foundParPayment, (int) interestToPay);
    }

    public void setLoanInRisk() {
        this.status = LoanStatus.InRisk;
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).payYaz == yaz.getCurrentTime()) {
                payments.get(i).shouldBePaid = true;
                int moneyShouldPay = payments.get(i).getTotalPay();
                if (i < payments.size() - 1)
                    payments.get(i + 1).totalPay += moneyShouldPay;
            }

        }
    }
    public int calcTotalInterestExp() {
        DecimalFormat df = new DecimalFormat("0.00");
        double interestParsToDouble = Double.parseDouble(df.format(this.interestPerPayment));
        double interestPercentage = (interestParsToDouble / 100);
        double interestToPay = this.capital * interestPercentage;

        return (int)interestToPay;
    }

    public void payLoan(boolean isPaidByCustomer) {
        Payment pay =  payments.stream().filter(x -> yaz.getCurrentTime() == (x.payYaz))
                .findAny()
                .orElse(null);

            if(pay!= null) {
                if (isPaidByCustomer) {
                    lastYazPaid = yaz.getCurrentTime();
                    paidFund += pay.fundCut;
                    paidInterest += pay.interestCut;
                    remainingInterest = ExpectedTotalInterest - paidInterest;
                    remainingFund = capital - paidFund;
                    pay.setPaid(true);
                } else {
                    setLoanInRisk();
                }
            }
            else
                throw new IllegalArgumentException("Something Want Wrong with The Pay, Please check and run again");
    }


    private class Payment {

        private final int fundCut;
        private final int interestCut;
        private int totalPay;

        private int payYaz;
        private boolean isPaid;
        private boolean shouldBePaid;

        public Payment(int fundCut, int interestCut) {
            this.fundCut = fundCut;
            this.interestCut = interestCut;
            totalPay = fundCut + interestCut;
            isPaid = false;
            shouldBePaid = false;
        }

        public int getFundCut() {
            return fundCut;
        }

        public int getInterestCut() {
            return interestCut;
        }

        public int getPayYaz() {
            return payYaz;
        }

        public void setPayYaz(int payYaz) {
            this.payYaz = payYaz;
        }

        public int setTotalPay(int amount) {
            return totalPay = amount;
        }

        public int getTotalPay() {
            return totalPay;
        }

        public boolean isPaid() {
            return isPaid;
        }

        public void setPaid(boolean paid) {
            isPaid = paid;
        }

        @Override
        public String toString() {
            return "Payment{" +
                    "payYaz=" + payYaz +
                    ", fundCut=" + fundCut +
                    ", interestCut=" + interestCut +
                    ", totalPay=" + totalPay +
                    '}';
        }
    }


}
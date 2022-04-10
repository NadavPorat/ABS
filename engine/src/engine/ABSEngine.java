package engine;

import categories.Categories;
import customer.Customer;
import loan.BankLoan;
import loan.LoanStatus;
import resources.jaxb.parser.ABSParser;
import resources.jaxb.schema.AbsDescriptor;
import time.ITimeUnit;
import time.TimeUnitABS;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ABSEngine implements Engine {

    private static Map<String, BankLoan> loans;
    private final ITimeUnit yaz;
    //todo filter
    private Categories categories;
    private Map<String, Customer> customers;
    private boolean isBuildFromFile;


    public ABSEngine() {
        yaz = TimeUnitABS.TimeUnitABS();
    }

    private static AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        String JAXB_XML_SCHEMA_PACKAGE_NAME = "resources.jaxb.schema";
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_SCHEMA_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }

    public static String provideInfoLoanStatusToCustomer(String loanId) {
        StringBuilder outM = new StringBuilder("Loan Status is : ");

        BankLoan currLoan = loans.get(loanId);
        outM.append(currLoan.getLoanStatus());
        switch (currLoan.getLoanStatus()) {
            case New:
                outM.append("This is New Loan");
                break;
            case Active:
                outM.append("Next yaz to pay is= ");
                outM.append(currLoan.getNextYazPayment());
                outM.append("Pay Amount(fund+interest)= ");
                outM.append(currLoan.calcPayment());
                break;
            case Pending:
                outM.append("Remained amount of money to collect= ");
                outM.append(currLoan.remainedMoneyToPending());
            case Finished:
                outM.append("yez Start= ");
                outM.append(currLoan.getYazActivated());
                outM.append("yez End=");
                outM.append(currLoan.getCloseYaz());
                break;
            case InRisk:
                outM.append("Number of UnPayments= ");
                outM.append(currLoan.getNumUnPayments());
                outM.append("In Total Worth= ");
                outM.append(currLoan.getUnPaymentsWorth());
                break;

        }
        return outM.toString();
    }

    public int getCurrentTime() {
        return yaz.getCurrentTime();
    }

    @Override
    public ArrayList<String> getCustomersNames() {
        return new ArrayList<>(customers.keySet());
    }

    @Override
    public Map<String, String> getCustomerAndBalanceList() {
        Map<String, String> map = new HashMap<>();
        for (String name : customers.keySet()) {
            String balance = customers.get(name).getBalance().toString();
            map.put(name, balance);
        }

        return map;
    }

    @Override
    public List<String> getListOfCategory() {
        return categories.getCategories();
    }

    @Override
    public boolean placementLoanByName(String customerID, int amount, ArrayList<String> loansNameToInvest) throws Exception {
        boolean isPlacement = true;
        Customer investor = customers.get(customerID);
        int amountParLoan = amount / loansNameToInvest.size();


        for (String loanName : loansNameToInvest) {
            BankLoan loan = loans.get(loanName);
            if (loan.getOwner().equals(investor.getCustomerName())) {
            throw new IllegalArgumentException("\nSorry You Can Invest In Yourself, Please Start The Process Again");
            }

            if (!investor.withdrawal(amountParLoan)) {
                isPlacement = false;
                break;
            } else {
                investor.getInvestedLoans().put(loanName, loan);
                loan.getLenders().put(investor, amountParLoan);
                loan.addToPaidFund(amountParLoan);

            }
            if (loan.getLoanStatus() == LoanStatus.Active) {
                Customer borrow = this.customers.get(loan.getOwner());
                if (!fromPendingToActive(loan, borrow)) {
                    isPlacement = false;
                    break;
                }
            }
        }
        return isPlacement;

    }

    public boolean fromPendingToActive(BankLoan loan, Customer borrow) {
        boolean isSet = true;
        if (!borrow.deposit(loan.getCapital())) {
            isSet = false;

        } else {
            borrow.getMyLoans().put(loan.getId(), loan);
        }
        return isSet;
    }

    @Override
    public ArrayList<String> advanceYazForward() {
        ArrayList<String> actionMassage = new ArrayList<>();
       ArrayList<BankLoan> filteredLoans =(ArrayList<BankLoan>) loans.values().stream().
               filter(p -> p.getLoanStatus() == LoanStatus.Active || p.getLoanStatus() == LoanStatus.InRisk )
               .collect(Collectors.toList());


        Collections.sort(filteredLoans, new Comparator<BankLoan>() {
            @Override
            public int compare(BankLoan loan1, BankLoan loan2) {
                if (loan1.getYazActivated() < loan2.getYazActivated())
                    return loan1.getYazActivated() - loan2.getYazActivated();
                else if (loan1.getYazActivated() == loan2.getYazActivated()) {
                    return loan1.getTotalPayForNextPayment() - loan2.getTotalPayForNextPayment();
                } else {
                    return loan1.getTotalYazTime() - loan2.getTotalYazTime();
                }
            }
        });

        for (BankLoan loan : filteredLoans) {
            if (!paymentDay(loan)) {
                throw new IllegalArgumentException("Payment for each loan is made in full or not made at all");
            } else
                actionMassage.add("Pay For '" + loan.getId() + "' Was Execute Successfully By " + loan.getOwner());
        }

        this.yaz.moveTimeForward(1);

        return actionMassage;
    }

    private boolean paymentDay(BankLoan loan) {
        boolean isPayLoanActivate = true;
        Customer borrow = customers.get(loan.getOwner());
        int amountToPay = loan.getMoneyToPay();
        if(amountToPay != -1)//if == -1, we know it is no pay day Now
        {
            if (amountToPay > borrow.getBalance()) {
                isPayLoanActivate = false;
                loan.setLoanInRisk();
                throw new IllegalArgumentException("The Owner Not Have Enough Money In The Account, Add This Pay To UnPaid Payments");
            } else {
                loan.payLoan(borrow.withdrawal(amountToPay)); // todo double check of in risk CAN Remove IF Want
                isPayLoanActivate = true;
            }
        }
      return isPayLoanActivate;
    }


    public void setCustomers(Map<String, Customer> customers) {
        this.customers = customers;
    }

    public void setCategories(categories.Categories categories) {
        this.categories = categories;
    }

    public void setLoans(Map<String, BankLoan> loans) {
        this.loans = loans;
    }

    @Override
    public boolean Withdrawal(String customerID, int amount) {
        boolean isWithdrawal = false;
        if (this.customers.containsKey(customerID)) {
            Customer customer = this.customers.get(customerID);

            if (customer.withdrawal(amount))
                isWithdrawal = true;
        }
        return isWithdrawal;
    }

    @Override
    public boolean Deposit(String customerID, int amount) {
        boolean isDeposit = false;
        if (this.customers.containsKey(customerID)) {
            Customer customer = this.customers.get(customerID);
            if (customer.deposit(amount))
                isDeposit = true;
        }
        return isDeposit;
    }


    //TODO improve the filters maybe Streams
    @Override
    public ArrayList<String> filterLoans(String customerID, int amount, String category, int interestParYaz, int minYazForLoan) {
        Map<String, BankLoan> filteredLoans = new HashMap<>();
        ArrayList<String> returnList = new ArrayList<>();
        for (BankLoan loan : loans.values()) {
            String loanID = loan.getId();
            if ((loan.getLoanStatus() == LoanStatus.New || loan.getLoanStatus() == LoanStatus.Pending) && loan != this.customers.get(customerID).getMyLoans().get(loanID)) {

                filteredLoans.put(loanID, loans.get(loanID));
            }
        }
        if (interestParYaz != -1) {
            for (BankLoan loans : filteredLoans.values()) {
                if (loans.getInterestPerPayment() < interestParYaz) {
                    filteredLoans.remove(loans.getId());
                }
            }
        }
        if (minYazForLoan != -1) {
            for (BankLoan loans : filteredLoans.values()) {
                if (loans.getTotalYazTime() < minYazForLoan) {
                    filteredLoans.remove(loans.getId());
                }
            }

        }
        if (!category.equals("anyCategory")) {
            for (BankLoan loans : filteredLoans.values()) {
                if (!loans.getCategory().equals(category)) {
                    filteredLoans.remove(loans.getId());
                }
            }
        }


        for (BankLoan loan : filteredLoans.values()) {
            returnList.add(loan.toString());
        }
        return returnList;
    }

    @Override
    public boolean buildFromXML(String path) {
        try {
            InputStream inputStream = new FileInputStream(new File(path));
            ///TODO add QA/////
            AbsDescriptor absDescriptor = deserializeFrom(inputStream);
            setCategories(ABSParser.paresCategories(absDescriptor));
            setCustomers(ABSParser.paresCustomers(absDescriptor));
            setLoans(ABSParser.paresLoans(absDescriptor));
            this.isBuildFromFile = true;
            yaz.initToYaz1(); // in each load file we beck the yaz to 1
        } catch (Exception e) {
            System.out.println(e.toString() + " please check your path and file again");

        }

        return isBuildFromFile;
    }

    @Override
    public StringBuilder printExistLoans() {
        int index = 1;
        StringBuilder outMessage = new StringBuilder();
        for (BankLoan loan : loans.values()) {
            outMessage.append(index);
            outMessage.append(". ");
            outMessage.append(loan.toString());
            outMessage.append('\n');
            index++;
        }
        return outMessage;
    }

    @Override
    public StringBuilder printCustomers() {
        int index = 1;
        StringBuilder outMessage = new StringBuilder();
        for (Customer customer : customers.values()) {
            outMessage.append(index);
            outMessage.append(". ");
            outMessage.append(customer.toString());
            outMessage.append('\n');
            index++;
        }
        return outMessage;
    }

    @Override
    public boolean isInitialized() {
        return this.isBuildFromFile;
    }

}

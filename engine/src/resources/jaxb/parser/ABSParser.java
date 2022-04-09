package resources.jaxb.parser;

import categories.Categories;
import customer.Customer;
import loan.BankLoan;
import resources.jaxb.schema.AbsCategories;
import resources.jaxb.schema.AbsCustomer;
import resources.jaxb.schema.AbsCustomers;
import resources.jaxb.schema.AbsDescriptor;
import resources.jaxb.schema.AbsLoan;
import resources.jaxb.schema.AbsLoans;

import java.util.HashMap;
import java.util.Map;

public abstract class ABSParser {
    public static Map<String, Customer> paresCustomers(AbsDescriptor descriptor) throws Exception {
        Map<String, Customer> customers = new HashMap<>();

        AbsCustomers absCustomers = descriptor.getAbsCustomers();
        for (AbsCustomer absCustomer : absCustomers.getAbsCustomer()) {
            if (!customers.containsKey(absCustomer.getName())) {
                Customer c = new Customer(absCustomer.getName(), absCustomer.getAbsBalance());
                customers.put(c.getCustomerName(), c);
            } else {
                throw new Exception("The Customer already exist in the system ");
            }
        }

        return customers;
    }

    public static Map<String, BankLoan> paresLoans(AbsDescriptor descriptor) throws Exception {
        Map<String, BankLoan> loans = new HashMap<>();

        AbsLoans absLoans = descriptor.getAbsLoans();
        for (AbsLoan absLoan : absLoans.getAbsLoan()) {
            if (!loans.containsKey(absLoan.getId())) {
                BankLoan loan = new BankLoan(absLoan.getAbsOwner(), absLoan.getAbsCategory(), absLoan.getAbsCapital(), absLoan.getAbsTotalYazTime(), absLoan.getAbsPaysEveryYaz(), absLoan.getAbsIntristPerPayment(), absLoan.getId());
                loans.put(loan.getId(), loan);
            } else {
                throw new Exception("The Loan already exist in the system");
            }
        }

        return loans;
    }

    public static Categories paresCategories(AbsDescriptor descriptor) throws Exception {

        AbsCategories absCategories = descriptor.getAbsCategories();
        return new Categories(absCategories.getAbsCategory());
    }
}

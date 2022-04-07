package engine.resources.jaxb.parser;

import engine.categories.Categories;
import engine.customer.Customer;
import engine.loan.Loan;
import engine.resources.jaxb.schema.AbsCategories;
import engine.resources.jaxb.schema.AbsCustomer;
import engine.resources.jaxb.schema.AbsCustomers;
import engine.resources.jaxb.schema.AbsDescriptor;
import engine.resources.jaxb.schema.AbsLoan;
import engine.resources.jaxb.schema.AbsLoans;

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

    public static Map<String, Loan> paresLoans(AbsDescriptor descriptor) throws Exception {
        Map<String, Loan> loans = new HashMap<>();

        AbsLoans absLoans = descriptor.getAbsLoans();
        for (AbsLoan absLoan : absLoans.getAbsLoan()) {
            if (!loans.containsKey(absLoan.getId())) {
                Loan loan = new Loan(absLoan.getAbsOwner(), absLoan.getAbsCategory(), absLoan.getAbsCapital(), absLoan.getAbsTotalYazTime(), absLoan.getAbsPaysEveryYaz(), absLoan.getAbsIntristPerPayment(), absLoan.getId());
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

package engine;

import engine.categories.Categories;
import engine.customer.Customer;
import engine.loan.Loan;
import engine.resources.jaxb.parser.ABSParser;
import engine.resources.jaxb.schema.AbsDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class ABSEngine implements Engine {


//todo filter
    private Categories Categories;
    private Map<String, Loan> loans;
    private Map<String, Customer> Customers;


    private static AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        String JAXB_XML_SCHEMA_PACKAGE_NAME = "engine.resources.jaxb.schema";
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_SCHEMA_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }

    public void setCustomers(Map<String, Customer> customers) {
        Customers = customers;
    }

    public void setCategories(engine.categories.Categories categories) {
        Categories = categories;
    }

    public void setLoans(Map<String, Loan> loans) {
        this.loans = loans;
    }

    @Override
    public Boolean Withdrawal(int amount) {
        return null;
    }

        @Override
    public Boolean Deposit(int amount) {
        return null;
    }

    @Override
    public boolean Loan() {
        return false;
    }

@Override
    public boolean buildFromXML(String path) {
        boolean isBuild = false;

        try {
            InputStream inputStream = new FileInputStream(new File(path));
            ///TODO add QA/////
            AbsDescriptor absDescriptor = deserializeFrom(inputStream);
            setCategories((Categories) ABSParser.paresCategories(absDescriptor));
            setCustomers((Map<String, Customer>) ABSParser.paresCustomers(absDescriptor));
            setLoans((Map<String, Loan>) ABSParser.paresLoans(absDescriptor));
            isBuild = true;
        } catch (Exception e) {
            System.out.println(e.toString() +" please check your path and file again" );

            //TODO not exit after this exp

        }


        return isBuild;
}

    @Override
    public String printExistLoans() {
        return this.loans.toString();

    }

    @Override
    public String printCustomers() {
        return this.Customers.toString();
    }

    public void printABS() {
        System.out.println("==================================== ");
        System.out.println("List of Customers: ");
        for (String keys : Customers.keySet()) {
            System.out.println(keys);
        }
        System.out.println("==================================== ");
        System.out.println("List of Loans: ");
        for (String keys : loans.keySet()) {

            System.out.println(keys);
        }
        System.out.println("==================================== ");
        System.out.println("List of Categories: ");
        System.out.println(Categories.toString());
    }

}

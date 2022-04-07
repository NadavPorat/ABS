package engine.loan;


import engine.customer.Customer;
import engine.utils.TimeUnitABS;

public class Loan {
    ///todo classs infos
    protected final String id;
    protected final String owner;
    protected final String category;
    protected final Integer totalYazTime;
    protected final Integer paysEveryYaz;
    protected final Integer interestPerPayment;
    protected final Integer capital;
    protected LoanStatus status;


    public Loan(String owner, String category, Integer capital, Integer totalYazTime, Integer paysEveryYaz, Integer interestPerPayment, String id) {
        this.owner = owner;
        this.category = category;
        this.capital = capital;
        this.totalYazTime = totalYazTime;
        this.paysEveryYaz = paysEveryYaz;
        this.interestPerPayment = interestPerPayment;
        this.id = id;
        this.status = LoanStatus.New;
       }

    public String getId() {
        return id;
    }


    public LoanStatus getStatus() {
        return status;
    }

    public Integer getTotalWorthLoan() {
        return (int) (capital + (capital * interestPerPayment));
    }




    @Override
    public String toString() {
        return "Loan{" +
                "Loan name='" + id + '\'' +
                ", category='" + category + '\'' +
                ", capital=" + capital +
                ", paysEveryYaz=" + paysEveryYaz +
                ", interestPerPayment=" + interestPerPayment +
                ", TotalWorthLoan=" + totalYazTime +
                ", status=" + status +
                '}';
    }
}

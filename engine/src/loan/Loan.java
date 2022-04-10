package loan;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Loan {

    protected final String id;
    protected final String owner;
    protected final String category;
    protected final Integer totalYazTime;
    protected final Integer paysEveryYaz;
    protected final Integer interestPerPayment;
    protected final Integer capital; //fund amount
    protected LoanStatus status;




    public Loan(String owner, String category, Integer capital, Integer totalYazTime, Integer paysEveryYaz, Integer interestPerPayment, String id) {
        this.owner = owner;
        this.category = category;
        this.capital = capital; ///loan found
        this.totalYazTime = totalYazTime;
        this.paysEveryYaz = paysEveryYaz;
        this.interestPerPayment = interestPerPayment;
        this.id = id;
        this.status = LoanStatus.New;
    }
    public String getOwner() {
        return owner;
    }
    public Integer getCapital() {
        return capital;
    }
    public String getId() {
        return id;
    }
    public LoanStatus getLoanStatus() {
        return status;
    }



    public String getCategory() {
        return category;
    }

    public Integer getTotalYazTime() {
        return totalYazTime;
    }

    public Integer getInterestPerPayment() {
        return interestPerPayment;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "Loan name= '" + id + '\'' +
                ", category= '" + category + '\'' +
                ", capital= " + capital +
                ", paysEveryYaz= " + paysEveryYaz +
                ", interestPerPayment= " + interestPerPayment +
                ", status= ";
    }

}

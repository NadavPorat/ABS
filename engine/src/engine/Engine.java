package engine;

public interface Engine {

    Boolean Withdrawal (int amount);
    Boolean Deposit (int amount);
    boolean Loan();
    boolean buildFromXML(String path);
    String printExistLoans();
    String printCustomers();


}

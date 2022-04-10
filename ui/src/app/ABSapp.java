package app;

import IO.InputOutput;
import IO.eListCustomerType;
import IO.eUserInput;
import engine.Engine;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class ABSapp {

    private Engine engine;

    public void init(Engine engine) {
        this.engine = engine;
    }

    public void run() {
        eUserInput userInput = eUserInput.INIT;
        InputOutput.welcome();
        while (userInput != eUserInput.QUIT) {

            userInput = InputOutput.mainMenu(engine.getCurrentTime());
            if (engine.isInitialized() || userInput.equals(eUserInput.LOAD) || userInput.equals(eUserInput.QUIT)) {
                switch (userInput) {
                    case LOAD:
                        loadXmlFile();
                        break;
                    case ExistLoans:
                        PrintExistLoans();
                        break;
                    case ListCustomers:
                        PrintExistCustomers();
                        break;
                    case Deposit:
                        deposit();
                        break;
                    case Withdrawal:
                        withdrawal();
                        break;
                    case InvestmentPlacement:
                        loansPlacement();
                        break;
                    case moveYazForward:
                        moveYazForward();
                        break;
                    case QUIT:
                        userInput = InputOutput.quit();
                        break;
                }
            } else {
                InputOutput.unloadedSystem();
            }
            if (!userInput.equals(eUserInput.QUIT)) {
                InputOutput.continueApp();
            }
        }
        InputOutput.printMsg("Goodbye!");
    }

    private void moveYazForward() {
        int beforeYaz = engine.getCurrentTime();
        InputOutput.printMsg("Hey! I Going To Move The Clock With One Yaz Forward");
        try {
            ArrayList<String> returnValueFromAdvance = engine.advanceYazForward();
            InputOutput.printMsg("You Succeed Move The Yaz Forward :) ");
            InputOutput.printMsg("The Yaz Before The Action Was - " + beforeYaz);
            InputOutput.printMsg("The Current Yaz is - " + engine.getCurrentTime());
            if(returnValueFromAdvance.size()>0) {
                InputOutput.printMsg("Here Is The List Of Actions Was Execute Part Of The Yaz Update: ");
                InputOutput.printMsg(returnValueFromAdvance.toString());
            }
        } catch (IllegalArgumentException e) {
            InputOutput.printMsg(e.getMessage());
        }
    }

    private void loansPlacement() {

        try {
            InputOutput.printMsg("Please Chose The Customer You Want To Place The Loan For(Type The Index Of The Customer) ");
            String customerID = InputOutput.chooseCustomerForLoan(engine.getCustomerAndBalanceList());
            InputOutput.printMsg("Please Insert Amount Of Money To Invest: ");
            int amount = InputOutput.getIntegerInput();
            int sum = Integer.parseInt(engine.getCustomerAndBalanceList().get(customerID));
            if(sum< amount) {
                throw new IllegalArgumentException("You Can Invest More Then Your Balance!, Please Try Again");
            }
            String category = InputOutput.chooseCategory(engine.getListOfCategory()); //opt
            int interestParYaz = InputOutput.chooseInterestParYaz(); //opt
            int minYazForLoan = InputOutput.chooseMinYazForLoan(); //opt
            InputOutput.printMsg("Please Choose Loan/Loans From The List If You Want Multiple Loans Please Type The Index Of The Loans with space (Ex:'1 2 5')");
            InputOutput.printMsg("If You Chose Multiple Loans The Amount Will Be Divided Equally As Much As Possible.\n");

            ArrayList<String> LoanNameToInvest = InputOutput.chooseLoanToInvest(engine.filterLoans(customerID, amount, category, interestParYaz, minYazForLoan));
            InputOutput.printMsg("You Chose The Following Loans: ");
            InputOutput.printMsg(LoanNameToInvest.toString());

            if (engine.placementLoanByName(customerID, amount, LoanNameToInvest)) {
                InputOutput.printMsg("You succeed Place The Loan/s, Have Good Day :) ");
            } else {
                InputOutput.printMsg("Sorry Something Want Wrong :( please Try Again ");
            }
        } catch (Exception e) {
            InputOutput.printMsg(e.getMessage());
        }
    }

    private void deposit() {
        InputOutput.printMsg("Please Chose The Customer You Want To Deposit The Money(insert by index of the customer): ");
        String customerID = InputOutput.chooseNameFromListCustomers(engine.getCustomersNames(), eListCustomerType.Deposit);
        InputOutput.printMsg("Please Insert Amount Of Money To Deposit: ");
        int amount = InputOutput.getIntegerInput();
        try {
            if (engine.Deposit(customerID, amount)) {
                InputOutput.printMsg("You succeed Deposit The Money :) ");
            } else {
                InputOutput.printMsg("Sorry Something Want Wrong With The Deposit :( please Try Again ");
            }
        } catch (IllegalArgumentException e) {
            InputOutput.printMsg(e.getMessage());
        }
    }

    private void withdrawal() {
        InputOutput.printMsg("Please Chose The Customer You Want To Withdrawal The Money(insert by index of the customer): ");
        String customerID = InputOutput.chooseNameFromListCustomers(engine.getCustomersNames(), eListCustomerType.Withdrawal);
        InputOutput.printMsg("Please Insert Amount Of Money To Withdrawal: ");
        int amount = InputOutput.getIntegerInput();
        try {
            if (engine.Withdrawal(customerID, amount)) {
                InputOutput.printMsg("You succeed Withdrawal The Money :) ");
            } else {
                InputOutput.printMsg("Sorry Something Want Wrong With The Withdrawal :( please Try Again ");
            }
        } catch (IllegalArgumentException e) {
            InputOutput.printMsg(e.getMessage());
        }

    }

    private void PrintExistLoans() {
        InputOutput.printMsg(engine.printExistLoans().toString());
    }

    private void PrintExistCustomers() {
        InputOutput.printMsg(engine.printCustomers().toString());

    }

    private void loadXmlFile() {
        String path = InputOutput.getXmlPath();
        if (path == null) {
            InputOutput.failedLoadSystem("The file path contains invalid letters.");
            return;
        }
        if (!InputOutput.isQuit(path)) {
            try {
                if (!Files.exists(Paths.get(path))) {
                    InputOutput.failedLoadSystem("The File you try to load is not exist.");
                    return;
                } else {
                    String file_type = Files.probeContentType(Paths.get(path));
                    if (!(file_type.equals("application/xml") || file_type.equals("text/xml"))) {
                        InputOutput.failedLoadSystem("The File you entered is not an xml file.");
                        return;
                    } else {
                        engine.buildFromXML(path);
                        InputOutput.successLoading();
                    }
                }
            } catch (FileNotFoundException ex) {
                InputOutput.failedLoadSystem("Wrong file path - the file you entered is not exist, try again.");
            } catch (Exception e) {
                InputOutput.failedLoadSystem(e.getMessage());
            }
        }
    }

}
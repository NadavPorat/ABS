package app;
import IO.UserInput;
import IO.inputOutput;
import engine.Engine;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ABSapp {

        private Engine engine;

        public void init(Engine engine) {
            this.engine = engine;
        }

        public void run() {
            UserInput userInput = UserInput.INIT;
            inputOutput.welcome();

            while (userInput != UserInput.QUIT) {
                userInput = inputOutput.mainMenu();
                if (userInput.equals(UserInput.LOAD) || userInput.equals(UserInput.QUIT)) {
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
                        case LoansPlacement:
                            loansPlacement();
                            break;
                        case QUIT:
                            userInput = inputOutput.quit();
                            break;
                    }
                } else {
                    inputOutput.unloadedSystem();
                }
                if (!userInput.equals(UserInput.QUIT)) {
                    inputOutput.continueApp();
                }
            }
            inputOutput.printMsg("Goodbye!");
        }

        private void loansPlacement() {
        }

        private void deposit() {
            int amount = inputOutput. getIntegerInput();
            engine.Deposit(amount);
        }
        private void withdrawal() {
            int amount = inputOutput. getIntegerInput();
            engine.Withdrawal(amount);
        }

        private void PrintExistLoans() {
            engine.printExistLoans();
        }
        private void PrintExistCustomers() {
            engine.printCustomers();
        }



        private void loadXmlFile() {
            String path = inputOutput.getXmlPath();
            if (path == null) {
                inputOutput.failedLoadSystem("The file path contains invalid letters.");
                return;
            }
            if (!inputOutput.isQuit(path)) {
                try {
                    if (!Files.exists(Paths.get(path))) {
                        inputOutput.failedLoadSystem("The File you try to load is not exist.");
                        return;
                    } else if (!Files.probeContentType(Paths.get(path)).equals("text/xml")) {
                        inputOutput.failedLoadSystem("The File you entered is not an xml file.");
                        return;
                    } else {
                        engine.buildFromXML(path);
                        inputOutput.successLoading();
                    }
                } catch (FileNotFoundException ex) {
                    inputOutput.failedLoadSystem("Wrong file path - the file you entered is not exist, try again.");
                } catch (Exception e) {
                    inputOutput.failedLoadSystem(e.getMessage());
                }
            }
        }

}

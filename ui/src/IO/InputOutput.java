package IO;

import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InputOutput {

    public static void welcome() {
        System.out.println("welcome to ABS - The next Generation For Manage Bank System");
    }

    public static eUserInput mainMenu(int yaz) {
        int input;

        System.out.println("=========== A.B.S System ===========");
        System.out.println("            Current Yaz: " + yaz + "\n");
        System.out.println(" 1. Load ABS system file\n" +
                " 2. Display Exist Loans in the system\n" +
                " 3. Display Exist Customers in the system\n" +
                " 4. Deposit Money By Name\n" +
                " 5. Withdrawal Money By Name\n" +
                " 6. Loans Placement\n" +
                " 7. TimeSet\n" +
                " 8. QUIT\n");
        System.out.println("(-- At each stage of the program, pressing '0' will return you to the main menu --)");
        System.out.print("Please choose an option: ");
        input = getIntegerInRange(1, 8);
        return eUserInput.values()[input];
    }

    private static int getIntegerInRange(int startRange, int endRange) {
        boolean validRange = false;
        int input;
        do {
            input = getIntegerInput();
            if (input >= startRange && input <= endRange) {
                validRange = true;
            } else {
                System.out.println("Wrong input - you entered a invalid choice, try again: ");
            }
        } while (!validRange);

        return input;
    }

    public static int getIntegerInput() {
        Scanner scanner = new Scanner(System.in);
        int value = 0;
        boolean validInput;

        do {
            try {
                value = scanner.nextInt();
                if (value == 0) { // quit
                    break;
                }
                validInput = true;
            } catch (InputMismatchException ex) {
                System.out.println("Wrong input - this is not a number, try again:");
                validInput = false;
                scanner.nextLine();
            }
        } while (!validInput);

        return value;
    }

    public static String getXmlPath() {
        String path;
        System.out.println("Enter the full path of the XML file you want to load:");
        path = getStringInput("path"); // return a valid path, yet no promise of existing file
        if (path.matches(".*[א-ת]+.*")) {
            return null;
        }
        if (path.equals("0")) {
            return eUserInput.QUIT.toString();
        }
        return path;
    }

    public static String getStringInput(String msg) {
        Scanner scanner = new Scanner(System.in);
        boolean valid;
        String input = null;
        do {
            try {
                input = scanner.nextLine();
                valid = true;
                if (input.equals("0")) {
                    input = eUserInput.QUIT.toString();
                }
            } catch (Exception ex) {
                System.out.println("Wrong input - this is not a valid " + msg + ", try again.");
                valid = false;
                scanner.nextLine();
            }
        } while (!valid);

        return input;
    }

    public static void failedLoadSystem(String s) {
        System.out.println("Loading the file ended with a failure:");
        System.out.println(s);
    }

    public static void successLoading() {
        System.out.println("The File loaded successfully!");
    }

    public static void targetNameRequest() {
        System.out.println("Please enter a target's name: (to return to the main menu, press '0')");
    }

    public static void printMsg(String s) {
        System.out.println(s);
    }

    public static void unloadedSystem() {
        System.out.println("The ABS System is not loaded yet, please load a Xml file first.");
    }

    public static int getProcessingTime() {
        int processingTime;
        System.out.println("Please Enter The Task Processing Time (for single target) in ms : ");
        processingTime = getIntegerInput();
        return processingTime;
    }

    public static void continueApp() {
        System.out.print("\nPress ENTER to continue ...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static boolean isQuit(String name) {
        if (name.equals(eUserInput.QUIT.toString())) {
            InputOutput.printMsg("Going back to main menu.");
            return true;
        }
        return false;
    }

    public static eUserInput quit() {
        System.out.println("The System is about the end");
        do {
            System.out.println("Are you sure you want to exit? (Y/N)");
            String answer = getStringInput("answer");
            switch (answer) {
                case "Y":
                    return eUserInput.QUIT;
                case "N":
                    return eUserInput.INIT;
                default:
                    System.out.println("This is not a valid answer, try again");
            }
        } while (true);
    }

    public static String chooseNameFromListCustomers(ArrayList<String> customers, eListCustomerType listType) {
        for (String str : customers) {
            System.out.println(customers.indexOf(str) + ". " + str);

        }
        int input = getIntegerInRange(0, customers.size() - 1);

        String name = customers.get(input);
        String answer;
        do {
            System.out.println("Are You Sure You Want To " + listType.toString() + " " + name + " Account ? (Y/N)");
            answer = getStringInput("answer");
            if (!answer.equals("Y")) {
                System.out.println("Ok Lets Try Again, Please Choosing The Correct Customer: ");
                input = getIntegerInRange(0, customers.size() - 1);
                name = customers.get(input);
            }
        } while (!answer.equals("Y"));

        return name;


    }

    public static String chooseCustomerForLoan(Map<String, String> customerAndBalanceMap) {
        ArrayList<String> nameList = new ArrayList<>(customerAndBalanceMap.keySet());
        for (String customerName : nameList) {

            System.out.println(nameList.indexOf(customerName) + ". " + customerName + ", Balance: " + customerAndBalanceMap.get(customerName));
        }
        int input = getIntegerInRange(0, nameList.size() - 1);
        String name = nameList.get(input);
        String answer;
        do {
            System.out.println("Are You Sure you Want To Choose " + name + " ? (Y/N)");
            answer = getStringInput("answer");
            if (!answer.equals("Y")) {
                System.out.println("Ok Lets Try Again, Please Choosing The Correct Customer: ");
                input = getIntegerInRange(0, nameList.size() - 1);
                name = nameList.get(input);
            }
        } while (!answer.equals("Y"));
        return name;
    }

    public static String chooseCategory(List<String> listOfCategory) {
        String choseUser = "anyCategory";
        String ifSkip;
        do {
            System.out.println("Are You Want To Choose Category For The Loan Y/N (capital letters):  ");
            ifSkip = getStringInput("answer");
            if (ifSkip.equals("Y") || ifSkip.equals("N")) {
                break;
            } else {
                System.out.println("Wrong Input Please Choose Y/N (capital letters):  ");
            }
        } while (true);

        if (ifSkip.equals("Y")) {
            for (String category : listOfCategory) {
                System.out.println(listOfCategory.indexOf(category) + ". " + category);

            }
            int input = getIntegerInRange(0, listOfCategory.size() - 1);

            choseUser = listOfCategory.get(input);
            String answer;
            do {
                System.out.println("Are You Sure You Want To " + choseUser.toString() + " Category? (Y/N)");
                answer = getStringInput("answer");
                if (!answer.equals("Y")) {
                    System.out.println("Ok Let's Try Again, Please Choosing The Correct Customer: ");
                    input = getIntegerInRange(0, listOfCategory.size() - 1);
                    choseUser = listOfCategory.get(input);
                }
            } while (!answer.equals("Y"));
        }

        return choseUser;

    }

    public static int chooseInterestParYaz() {
        int choseUser = -1;
        String ifSkip;
        do {
            System.out.println("Are You Want To Choose Interest Par Yaz For The Loan Y/N (capital letters):  ");
            ifSkip = getStringInput("answer");
            if (ifSkip.equals("Y") || ifSkip.equals("N")) {
                break;
            } else {
                System.out.println("Wrong Input Please Choose Y/N (capital letters):  ");
            }
        } while (true);


        if (ifSkip.equals("Y")) {
            System.out.println("Type Interest Par Yaz in numeric between (0-100]:  ");
            int input = getIntegerInput();
            String answer;
            do {
                System.out.println("Are You Sure You Want To " + input + " Interest Par Yaz? (Y/N)");
                answer = getStringInput("answer");
                if (!answer.equals("Y")) {
                    System.out.println("Ok Let's Try Again, Please Choosing The Correct Customer: ");
                    input = getIntegerInput();
                }
                choseUser = input;
            } while (!answer.equals("Y"));
        }// else user want skip this opt we use the -1 as skip value
        return choseUser;
    }

    public static int chooseMinYazForLoan() {
        int choseUser = -1;
        String ifSkip;
        do {
            System.out.println("Are You Want To Choose Min Yaz For The Loan Y/N (capital letters):  ");
            ifSkip = getStringInput("answer");
            if (ifSkip.equals("Y") || ifSkip.equals("N")) {
                break;
            } else {
                System.out.println("Wrong Input Please Choose Y/N (capital letters):  ");
            }
        } while (true);

        if (ifSkip.equals("Y")) {
            System.out.println("Type Min Yaz For The Loan(in numeric):  ");
            int input = getIntegerInput();
            String answer;
            do {
                System.out.println("Are You Sure You Want To " + input + " Interest Par Yaz? (Y/N)");
                answer = getStringInput("answer");
                if (!answer.equals("Y")) {
                    System.out.println("Let's Try Again, Please Choosing The Correct Customer: ");
                    input = getIntegerInput();
                }
                choseUser = input;
            } while (!answer.equals("Y"));
        } // else user want skip this opt we use the -1 as skip value
        return choseUser;
    }

    private static ArrayList<Integer> getListLoansFromUser(int startIndex, int endIndex) {
        String userInput;
        Scanner scanner = new Scanner(System.in);

        ArrayList<Integer> choseLoansIndex = new ArrayList<Integer>();
        boolean valid = true;
        do {
            userInput = scanner.nextLine();
            String[] input = userInput.split(" ");
            for (String str : input) {
                int choseIndex = Integer.parseInt(str);
                if (choseIndex < startIndex || choseIndex > endIndex) {
                    System.out.println(choseIndex + " Input isn't Valid Please Choose Numbers Between " + startIndex + "-" + endIndex + " Please Insert Again All numbers");
                    valid = false;
                }
                else {
                    choseLoansIndex.add(choseIndex);
                    valid = true;
                }
            }
        } while (!valid);
        return choseLoansIndex;
    }

    public static ArrayList<String> chooseLoanToInvest(List<String> filterLoans) {
        String loanName;
        ArrayList<String> nameOfChoosingLoans = new ArrayList<>();
        for (String loan : filterLoans) {
            System.out.println(filterLoans.indexOf(loan) + ". " + loan);
        }
        ArrayList<Integer> choseLoansIndex = getListLoansFromUser(0, filterLoans.size() - 1);

        for (Integer index : choseLoansIndex) {
            loanName = filterLoans.get(index).split(",")[0];
            loanName = loanName.substring(8);
            loanName = loanName.substring(1, loanName.length()-1);
            nameOfChoosingLoans.add(loanName);
        }
        return nameOfChoosingLoans;
    }
}
//TODO what if user didnt choose any


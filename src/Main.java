import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static void addInterest(Account account) {
        if (account.getBalance() > 10000) {
            account.setBalance(account.getBalance() * 1.05);
            System.out.println("5% interest applied! Keep balance above Php 10000 to enjoy more benefits. ");
        }
    }

    public static void chargePenalty(Account account) {
        if (account.getBalance() < 5000) {
            account.setBalance(account.getBalance() * 0.98);
            System.out.println("Account was penalized for not maintaining Php 5000. ");
        }
    }

    public static Account createAccount() {
        System.out.println("[ Creating an account... ] ");

        String name;
        while (true) {
            System.out.print("Name of the account \n>> ");
            name = scanner.nextLine().trim();
            if (name.isBlank()) {
                System.out.println("Invalid name, try again.");
                continue;
            }
            break;
        }

        double balance;
        while (true) {
            System.out.print("Initial amount of deposit (minimum of Php 5000) \n>> Php ");
            try {
                balance = scanner.nextDouble();
                if (balance < 5000) {
                    System.out.println("Minimum of Php 5000, try again. ");
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Invalid input, try again. ");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            break;
        }
        return new Account(name, balance);
    }

    public static boolean askPin(Account account) {
        int tries = 0;
        String input;

        while (tries != 3) {
            System.out.println("[ Login ] ");
            System.out.print("Input your PIN: ");
            try {
                input = scanner.nextLine();
                if (!(input.equals(account.getPin()))) {
                    tries += 1;
                    System.out.format("Wrong PIN! %d tries left... ", 3-tries);
                    System.out.println();
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static char askTransact() {
        char[] validInput = {'c','d','w','h','q'};
        char transactChoice;
        while (true) {
            System.out.println("\n================ \nMadATM is Online \n================ ");
            System.out.println("[C]heck Balance ");
            System.out.println("[D]eposit Cash ");
            System.out.println("[W]ithdraw Cash ");
            System.out.println("C[H]hange PIN ");
            System.out.println("[Q]uit ");
            System.out.println();
            System.out.print("Transaction [C,D,W,H,Q]: ");
            transactChoice = scanner.nextLine().toLowerCase().charAt(0);

            for (char c: validInput)
                if (c == transactChoice)
                    return transactChoice;

            System.out.println("Invalid choice, try again. ");
        }
    }

    public static void main(String[] args) {
        Account account = createAccount();
        System.out.println("Account created! Proceed to login. ");
        while (true) {
            System.out.println("\n================== \nWelcome to MadATM! \n================== ");
            if (askPin(account)) {
                System.out.println("Logging you in... ");
            } else {
                System.out.println("3 failed attempts, forcing system shutdown... \n\n");
                System.exit(0);
            }

            while (true) {
                char transact = askTransact();
                System.out.println("\n==================");
                if (transact == 'c') {
                    System.out.println("Account name:    " + account.getName());
                    System.out.println("Account balance: Php " + account.checkBal());

                } else if (transact == 'd') {
                    while (true) {
                        System.out.println("Account balance: Php " + account.getBalance());
                        System.out.print("Enter the amount of deposit: Php ");
                        try {
                            double deposit_amt = scanner.nextDouble();
                            if ((deposit_amt <= 0))
                                throw new Exception();
                            account.deposit(deposit_amt);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Invalid input, try again. ");
                            scanner.nextLine();
                            continue;
                        }
                        addInterest(account);
                        System.out.println("[ updated ] Account balance: Php " + account.getBalance());
                        scanner.nextLine();
                        break;
                    }

                } else if (transact == 'w') {
                    while (true) {
                        System.out.println("Account balance: Php " + account.getBalance());
                        System.out.print("Enter the amount to be withdrawn: Php ");
                        try {
                            double withdraw_amt = scanner.nextDouble();
                            if ((withdraw_amt < 0) || withdraw_amt > account.getBalance())
                                throw new Exception();
                            account.withdraw(withdraw_amt);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Invalid input or insufficient funds, try again. ");
                            scanner.nextLine();
                            continue;
                        }
                        chargePenalty(account);
                        System.out.println("[ updated ] Account balance: Php " + account.getBalance());
                        scanner.nextLine();
                        break;
                    }

                } else if (transact == 'h') {
                    System.out.print("Input your CURRENT PIN: ");
                    String currentPinInput = scanner.nextLine();
                    System.out.print("Input your NEW PIN: ");
                    String newPinInput = scanner.nextLine();
                    System.out.print("Verify NEW PIN: ");
                    String verificationInput = scanner.nextLine();
                    if (currentPinInput.equals(account.getPin()) && (newPinInput.equals(verificationInput))) {
                        account.changePin(newPinInput);
                        System.out.println("Your PIN was changed successfully. Please re-login. ");
                        break;
                    } else {
                        System.out.println("Incorrect details, PIN change was unsuccessful. ");
                    }

                } else {
                    System.out.println("Thank you for banking with us! Goodbye... \n\n");
                    System.exit(0);
                }
            }
        }
    }
}
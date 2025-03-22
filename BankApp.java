import java.util.Scanner;
import java.text.DecimalFormat;

// Custom Exception for Insufficient Funds
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// BankAccount Class
class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    // Withdraw Method with Exception Handling
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        balance -= amount;
        System.out.println("Withdrawal successful! New balance: " + formatCurrency(balance));
    }

    // Getter for balance with currency formatting
    public String getBalance() {
        return formatCurrency(balance);
    }

    // Change visibility of formatCurrency to public
    public String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("$#,##0.00");
        return formatter.format(amount);
    }
}

// Main Class to Test the Program
public class BankApp {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(50000000000000000.00);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Initial Balance: " + account.getBalance());

        // Continuous loop for withdrawals
        while (true) {
            System.out.print("\nAccount balance: " + account.getBalance());
            System.out.print("\nEnter amount to withdraw or type 'exit' to quit: ");

            // Check if user wants to exit
            if (scanner.hasNext("exit")) {
                System.out.println("Exiting program. Final balance: " + account.getBalance());
                break;
            }

            try {
                // Checking for non-double inputs and handling them
                if (!scanner.hasNextDouble()) {
                    System.out.println("Invalid entry, try again.");
                    scanner.next(); // Clear the invalid input
                    continue;
                }

                double userAmount = scanner.nextDouble();
                System.out.println("\nAttempting to withdraw " + account.formatCurrency(userAmount));
                account.withdraw(userAmount);

            } catch (InsufficientFundsException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}

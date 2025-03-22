public class Gambler {
    private double balance;

    public Gambler(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void placeBet(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds to place this bet.");
        }
    }

    public void addWinnings(double winnings) {
        balance += winnings;
    }
}

import java.util.*;

public class HorseRaceGambling {
    public static void main(String[] args) {
        // Create horses
        List<Horse> horses = new ArrayList<>();
        horses.add(new Horse("Splashy", 8.0));   // 25% chance of winning
        horses.add(new Horse("Thunder", 8.0));   // 20% chance of winning
        horses.add(new Horse("Blaze", 8.0));    // 15% chance of winning
        horses.add(new Horse("Storm", 8.0));    // 12% chance of winning
        horses.add(new Horse("Flash", 8.0));    // 10% chance of winning
        horses.add(new Horse("Bolt", 8.0));     // 8% chance of winning
        horses.add(new Horse("Whirlwind", 8.0)); // 6% chance of winning
        horses.add(new Horse("Gale", 8.0));     // 4% chance of winning


        // Create gambler with initial balance
        Gambler gambler = new Gambler(1000000.0); // Start with $1000000

        // Simulate betting and racing
        Scanner sc = new Scanner(System.in);
        boolean continuePlaying = true;

        while (continuePlaying && gambler.getBalance() > 0) {
            System.out.println("Your current balance: $" + gambler.getBalance());
            System.out.println("Place your bet on a horse:");

            // Show horses and odds
            for (int i = 0; i < horses.size(); i++) {
                Horse horse = horses.get(i);
                System.out.println((i + 1) + ". " + horse.getName() + " (Odds: " + horse.getOdds() + ")");
            }

            int horseChoice = sc.nextInt() - 1;
            System.out.println("Enter your bet amount:");
            double betAmount = sc.nextDouble();

            if (betAmount > gambler.getBalance()) {
                System.out.println("You don't have enough money for this bet.");
                continue;
            }

            gambler.placeBet(betAmount);

            // Run the race and get the winning horse
            Horse winner = runGraphicalRace(horses);
            System.out.println("The winner is: " + winner.getName());

            // Check if the player's bet was correct
            if (horses.get(horseChoice).equals(winner)) {
                double winnings = betAmount * horses.get(horseChoice).getOdds();
                System.out.println("You won! You earned $" + winnings);
                gambler.addWinnings(winnings);
            } else {
                System.out.println("You lost the bet.");
            }

            System.out.println("Would you like to race again? (yes/no)");
            String answer = sc.next();
            continuePlaying = answer.equalsIgnoreCase("yes");
        }

        if (gambler.getBalance() <= 0) {
            System.out.println("You have run out of money!");
        } else {
            System.out.println("Thanks for playing! Your final balance is $" + gambler.getBalance());
        }

        sc.close();
    }

    // Graphical race function - returns the winning horse
private static Horse runGraphicalRace(List<Horse> horses) {
    Random rand = new Random();
    int trackLength = 50; // Number of characters for the race track
    boolean raceFinished = false;

    // Initialize horse positions
    int[] positions = new int[horses.size()];
    Arrays.fill(positions, 0);

    Horse winner = null;

    // Race loop
    while (!raceFinished) {
        // Clear the console for a fresh frame
        try {
            Thread.sleep(300); // Add a delay for better visualization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("\033[H\033[2J"); // Clear the console (for Unix-like terminals)
        System.out.flush();

        // Print the track for each horse
        for (int i = 0; i < horses.size(); i++) {
            String track = "|";
            for (int j = 0; j < positions[i]; j++) {
                track += "-"; // Progress of the horse
            }
            track += horses.get(i).getName(); // Add horse's name at the end
            for (int j = positions[i]; j < trackLength; j++) {
                track += " "; // Empty space left on the track
            }
            track += "|"; // Finish line

            System.out.println(track);
        }

        // Move each horse forward based on their odds
        for (int i = 0; i < horses.size(); i++) {
            double odds = horses.get(i).getOdds();
            double probabilityOfMove = 1 / odds; // Probability of moving based on odds

            // Move the horse forward with weighted probability
            if (rand.nextDouble() < probabilityOfMove) {
                positions[i] += rand.nextInt(3); // Randomly move the horse 0 to 2 steps
            }

            // Check if this horse crossed the finish line
            if (positions[i] >= trackLength) {
                raceFinished = true;
                winner = horses.get(i); // Set the winner to this horse
            }
        }
    }

    return winner; // Return the horse that won the race
}

}

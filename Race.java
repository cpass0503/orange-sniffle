import java.util.List;
import java.util.Random;

public class Race {
    private List<Horse> horses;

    public Race(List<Horse> horses) {
        this.horses = horses;
    }

    public Horse startRace() {
        Random rand = new Random();
        Horse winner = null;

        for (Horse horse : horses) {
            if (horse.race()) {
                winner = horse;
                break;
            }
        }

        // If no horse won based on odds, pick one randomly (ensures a result)
        if (winner == null) {
            winner = horses.get(rand.nextInt(horses.size()));
        }
        return winner;
    }
}

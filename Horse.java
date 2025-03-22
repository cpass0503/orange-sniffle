
public class Horse {
    private String name;
    private double odds;

    public Horse(String name, double odds) {
        this.name = name;
        this.odds = odds;
    }

    public String getName() {
        return name;
    }

    public double getOdds() {
        return odds;
    }

    // Simulates the horse's chance of winning based on odds
    public boolean race() {
        return Math.random() < (1 / odds); // Higher odds give lower chance of winning
    }
}


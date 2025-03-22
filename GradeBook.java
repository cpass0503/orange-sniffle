public class GradeBook {
    public int lowestScore(int[] scores) {
        int current = scores[0];
        for (int score : scores) {
            if (score < current) {
                current = score;
            } else {
                continue;
            }
        }
        return current;
    }
    public float newAverage(int[] scores) {
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return (float) (sum - lowestScore(scores))/(scores.length - 1);
    }
    public static void main(String[] args) {
        int[] scores = {10, 11, 12, 6, 9, 12, 11};
        GradeBook gb = new GradeBook();
        System.out.println("Dropped quiz score: " + gb.lowestScore(scores) + ". New average: " + gb.newAverage(scores));

    }
}
import java.io.*;


public class QuizScores {
    public static double[] readFile() {
        double[] scores = new double[24];
        int index = 0;
        try(BufferedReader br = new BufferedReader(new FileReader("quizScores.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    scores[index] = Double.parseDouble(line);
                    if (scores[index] > 12 || scores[index] < 0) {
                        System.out.println("Format error on line " + (index + 1) + ": " + line);
                    }
                    index++;
                } catch (NumberFormatException e) {
                    System.out.println("Format error on line " + (index + 1) + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error accessing/reading file");
        } 
        return scores;
    }


    public static void main(String[] args) {
        double[] scores = readFile();
        for (double s : scores) {
            System.out.println(s);
        }
    }
}

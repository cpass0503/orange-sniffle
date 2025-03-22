import java.io.*;
import java.util.*;

public class ReadingFiles {
    public static void main(String[] args) {
        String filePath = "studentScores.txt"; // Your file path

        Map<String, Integer> scores = new HashMap<>();
        List<String> validLines = new ArrayList<>();

        readAndProcessFile(filePath, scores, validLines);
        writeValidLinesToFile(filePath, validLines);
        printSortedScores(scores);
    }

    // Function to read and process the file
    private static void readAndProcessFile(String filePath, Map<String, Integer> scores, List<String> validLines) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line, scores, validLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to process a single line
    private static void processLine(String line, Map<String, Integer> scores, List<String> validLines) {
        if (line.contains(":")) {
            handleColonLine(line, scores, validLines);
        } else {
            handleNoColonLine(line, scores, validLines);
        }
    }

    // Function to handle lines with a colon
    private static void handleColonLine(String line, Map<String, Integer> scores, List<String> validLines) {
        String[] parts = line.split(": ");
        String name = parts[0].trim();
        try {
            int score = Integer.parseInt(parts[1].trim());
            scores.put(name, score);
            validLines.add(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid score for " + name + ": " + parts[1].trim());
        }
    }

    // Function to handle lines without a colon
    private static void handleNoColonLine(String line, Map<String, Integer> scores, List<String> validLines) {
        String[] parts = line.split(" ");
        if (parts.length >= 2) {
            String name = parts[0].trim();
            try {
                int score = Integer.parseInt(parts[1].trim());
                String correctedLine = name + ": " + score;
                validLines.add(correctedLine);
                scores.put(name, score);
                System.out.println("Corrected line: " + correctedLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid score for " + name + ": " + parts[1].trim());
            }
        } else {
            System.out.println("Skipping invalid line: " + line);
        }
    }

    // Function to write valid lines back to the file
    private static void writeValidLinesToFile(String filePath, List<String> validLines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String validLine : validLines) {
                bw.write(validLine);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to print sorted scores
    private static void printSortedScores(Map<String, Integer> scores) {
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        for (Map.Entry<String, Integer> entry : sortedScores) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

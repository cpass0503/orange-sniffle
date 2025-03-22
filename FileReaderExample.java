import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderExample {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("studentScores.txt"))) {
            while (reader.readLine() != null) {
            }
            System.out.println("File read successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;

// public class FileReaderExample {
//     public static void readExampleFile() {
//         // Specify the file name
//         String fileName = "studentScores.txt";

//         // Try-with-resources to handle file closing automatically
//         try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 // Read each line, but do nothing with it

//             }
//             System.out.println("File read successfully.");
//         } catch (IOException e) {
//             System.out.println("An error occurred while reading the file: " + e.getMessage());
//         }
//     }

//     public static void main(String[] args) {
//         readExampleFile();
//     }
// }


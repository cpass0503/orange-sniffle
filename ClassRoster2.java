import java.io.*;


public class ClassRoster2 {
    public static void readFile(String[] first, String[] last) {
        int index = 0;
        try(BufferedReader br = new BufferedReader(new FileReader("classRoster.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    System.out.println("Invalid format on line " + (index + 1) + ": " + line);
                } else {
                    last[index] = parts[0];
                    first[index] = parts[1].trim();
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error accessing/reading file");
        }
    }


    public static void main(String[] args) {
        String[] first = new String[12];
        String[] last = new String[12];
        readFile(first, last);
        for (int i = 0; i < 12; i++) {
            System.out.println(first[i] + " " + last[i]);
        }
    }
}
import java.io.*;

public class Temperatures {
    public static int[] readFile() {
        int[] temps = new int[20];
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("temps.txt"))) {
            for (i = 0; i < 20; i++) {
                String line = br.readLine();
                if (line == null) break;
                temps[i] = Integer.parseInt(line.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        } catch (IOException e) {
            System.out.println("Error reading the file.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format on line " + (i + 1));
        }
        return temps;
    }

    public static void main(String[] args) {
        for (int t : readFile()) System.out.println(t);
    }
}

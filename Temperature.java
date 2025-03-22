import java.io.*;


public class Temperature {
    public static int[] readFile() {
        int[] temps = new int[20];
        int index = 0;
        try(BufferedReader br = new BufferedReader(new FileReader("temps.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    temps[index] = Integer.parseInt(line);
                    index++;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid format on line " + (index + 1) + ": " + line);
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error finding/reading file");
        }
        return temps;
    }


    public static void main(String[] args) {
        int[] temps = readFile();
        for(int t : temps) {
            System.out.println(t);
        }
    }
}

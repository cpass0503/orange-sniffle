import java.util.ArrayList;
import java.util.Scanner;


public class NameFormatter {
    public static void main(String[] args) {
        ArrayList<String> names = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);


        // Input names into the ArrayList
        System.out.println("Enter names (type 'done' to stop):");
        while (true) {
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) {
                break;
            }
            names.add(name);
        }


        // Format names
        formatNames(names);


        // Output the formatted names
        System.out.println("\nFormatted Names:");
        for (String name : names) {
            System.out.println(name);
        }


        scanner.close();
    }


    private static void formatNames(ArrayList<String> names) {
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String formattedName = formatName(name);
            if (!name.equals(formattedName)) {
                System.out.println("Correcting: " + name + " -> " + formattedName);
                names.set(i, formattedName);
            }
        }
    }




    private static String formatName(String name) {
        if (name == null || name.isEmpty()) {
            return name; // Return as is if the name is null or empty
        }
        // Convert to lowercase, then capitalize the first letter
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}





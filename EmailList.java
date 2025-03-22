import java.io.*;
import java.util.*;


public class EmailList {
    public static String[] readFile () {
        String[] usernames = new String[50];
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("emailList.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("@");
                if (parts.length == 2){
                    usernames[index] = parts[0];
                } else {
                    System.out.println("Format error on line " + (index + 1));
                }
            }
            index++;
        } catch (IOException e) {
            System.out.println("Error accessing file");
        }
        return usernames;
    }
    public static void checkName(String username) {
        boolean validName = true;
        String[] usedNames = readFile();
        for (String name : usedNames) {
            if (name.equalsIgnoreCase(username)) {
                System.out.println("user name is " + username + 1);
                validName = false;
            }
        }
        if (validName == true)
            System.out.println("user name is " + username );
    }


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
        String[] parts = name.split(" ");
        String username = name.substring(0,1) + parts[1];
        checkName(username);
    }
}

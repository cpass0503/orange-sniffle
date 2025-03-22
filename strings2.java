/*
 *  In this program we will use loops on Strings
 *  If a character in a string is a vowel, print it twice.
 */
import java.util.Scanner;


public class strings2 {
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = input.nextLine();
        for (int i = 0; i < name.length(); i++)
        {
            char c = name.charAt(i);
            // c is the specific character at index i of name
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
            {
                System.out.print(c + c);
                // print ascii numbers
                System.out.print(Character.toString(c) + Character.toString(c));
            }
            else
            {
                System.out.print(c + 0);
                // can print single character if not involved with any operations
                System.out.print(Character.toString(c));
            }
        }
        System.out.println();
    }
}

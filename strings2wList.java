/*
 *  In this program we will use loops on Strings
 *  If a character in a string is a vowel, print it twice.
 */
import java.util.Scanner;


public class strings2wList {
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = input.nextLine();
        char[] vowels = {'A', 'a', 'E', 'e', 'I', 'i', 'O', 'o', 'U', 'u'};

        for (int i = 0; i < name.length(); i++)
        {
            char c = name.charAt(i);
            boolean vowel = false;
            for (char letter : vowels)
            {
                if (letter == c)
                {
                    vowel = true;
                }
            }
            if (vowel)
            {
                System.out.print(Character.toString(c) + Character.toString(c));
            }
            else
            {
                System.out.print(c);
            }
        }
        System.out.println();
    }
}

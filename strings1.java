/*
 * The purpose of this code is to introduce you to the different methods of the String class.
 * Methods covered are: length, equals, indexOf, and substring
 */


import java.util.Scanner;
public class strings1 {
    public static void main(String[] args)
    {
        Scanner kboard = new Scanner(System.in);
        System.out.print("Enter your first name: ");
        String firstName = kboard.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = kboard.nextLine();
        System.out.print("Enter your gender: ");
        String response = kboard.nextLine();


        String title;
        if (response.equalsIgnoreCase("male"))
        {
            title = "Sir";
        }
        else if (response.equalsIgnoreCase("female"))
        {
            title = "Lady";
        }
        else
        {
            title = "Person";
        }
        String name = firstName + ' ' + lastName;


        System.out.println("Hello there " + title + ' ' + name);


        System.out.print("Enter a substring: ");
        String part = kboard.nextLine();
        if (name.indexOf(part) >= 0)
        // name.indexOf(part) will return -1 if doesnt exist
        {
            System.out.println(part + " is in " + name);
        }
        else
        {
            System.out.println(part + " is not in " + name);
        }


        int end, start;
        do
        {
            System.out.print("Pick starting point of substring: ");
            start = kboard.nextInt();
        }
        while (start < 0 || start >= name.length());
        do
        {


            System.out.print("Pick ending  point of substring: ");
            end = kboard.nextInt();
        }
        while (end < start || end >= name.length());
        String sub = name.substring(start, end);
        System.out.println(sub + " is a substring of " + name);
    }
}

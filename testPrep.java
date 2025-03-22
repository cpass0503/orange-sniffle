import java.util.Scanner;
public class testPrep {
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        String blass = scan.nextLine();

        if (name.length() < 8) {
            System.out.println("Name is too short");
        } else {
            System.out.println("Name: " + name);
        }

        int x = blass.indexOf("Science");

        if (x == -1) {
            System.out.println(blass + " is not a science class");
        } else {
            System.out.println("Science class: " + blass.substring(0, x));
        }

    }
}
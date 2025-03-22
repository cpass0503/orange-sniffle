import java.util.Scanner;
public class substringCheck {
    public static void main(String[] args)
    {
        String CHOICE = "Sigma Fanum Tax Skibidi Rizz Rizzler Livvy Dunne Baby Gronk Toilet Galvanized Square Steel Ohio Duke Dennis";
        Scanner scan = new Scanner(System.in);
        System.out.println(CHOICE);
        String input = "";
        do
        {
            System.out.print("Enter a substring from the string that is 3-6 characters long: ");
            input = scan.nextLine();
        } while (input.length() > 6 || input.length() < 3);

        if (CHOICE.toLowerCase().contains(input.toLowerCase()))
        {
            System.out.println(input + " is in " + CHOICE);
            System.out.println("Good Job!");
        }
        else {
            System.out.println("womp womp, ur not brainrotted enough");
        }


    }
}
import java.util.Scanner;
public class birthdays {
    public static void main(String[] args)
    {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String month = "";
        int day;
        int year;
        boolean monthCheck = false;
        

        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("Enter your birth month: ");
            month = scan.nextLine();
            for (String monthInput : months) {
                if (month.equals(monthInput)) {
                    monthCheck = true;
                }
            }
        } while (monthCheck != true);

        do {
            System.out.print("Enter the day of the month on which you were born: ");
            day = scan.nextInt();
        } while (!(day <= 31 && day >= 1));

        do{
            System.out.print("Enter your birth year: ");
            year = scan.nextInt();
        } while (!(year >= 1908 && year <= 2024));

        String dob = month + " " + day + ", " + year;
        System.out.println(dob);


    }
}
import java.util.Scanner;
public class numbers {
    public static void main(String[] args)
    {
        int mpgOLD = 24;
        int mpgNew = 35;
        double gasCostOld = 3.45;
        double gasCostNew = 2.20;
        int miles = 612;

        double costOld = Math.round((((double) miles / mpgOLD) * gasCostOld) * 100) / 100;
        double costNew = Math.round((((double) miles / mpgNew) * gasCostNew) * 100) / 100;
        if (costOld > costNew) {
            System.out.println("New Car is less expensive");
        } else if (costOld < costNew) {
            System.out.println("Old Car is less expensive");
        } else {
            System.out.println("Gas expenses for both cars are the same");
        }


        System.out.println(costOld);
        System.out.println(costNew);





    }
}

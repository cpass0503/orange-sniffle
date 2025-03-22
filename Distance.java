import java.util.Scanner;

public class Distance {
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    public Distance()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter x value of first point: ");
        x1 = scan.nextInt();

        System.out.print("Enter y value of first point: ");
        y1 = scan.nextInt();

        System.out.print("Enter x value of second point: ");
        x2 = scan.nextInt();

        System.out.print("Enter y value of second: ");
        y2 = scan.nextInt();
    }

    public void straightDistance()
    {
      int delta_y = y2-y1;
      int delta_x = x2-x1;
      double straight_distance = Math.sqrt((delta_x*delta_x)+(delta_y*delta_y));
      System.out.println("The straight distance is " + straight_distance);
    }

    public void manhattanDistance()
    {
        int firstDist = Math.abs(x1 - x2);
        int secondDist = Math.abs(y1 - y2);

        int manhattanDist = firstDist + secondDist;
        System.out.println("The Manhattan Distance is: " + manhattanDist);
    }
    public void equationOfLine()
    {
        int firstDist = Math.abs(x1 - x2);
        int secondDist = Math.abs(y1 - y2);

        double slope = (double) secondDist / firstDist;

        System.out.println("Line Equation: y - " + y1 + " = " + slope + "(x - " + x1 + ")");
    }
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        Distance points = new Distance();

        points.manhattanDistance();
        points.equationOfLine();
        points.straightDistance();

    }

}

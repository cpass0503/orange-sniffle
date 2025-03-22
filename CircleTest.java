import java.util.Scanner;


public class CircleTest
{
  public static void main(String[] args)
  {
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter the radius of the circle: ");
    int radius = scan.nextInt();


    Circle circle = new Circle(radius);
    Circle c1 = new Circle();
// Call the getRadius method to determine the radius of the two circles.
int circleRadius = circle.getRadius();
int c1Radius = c1.getRadius();
// Call the getArea method to determine the area of the two circles.
double circleArea = circle.getArea();
double c1Area = c1.getArea();
    // Call the getCircumference method you wrote in the Circle Class.
    double circleCircumference = circle.getCircumference();
    double c1Circumference = c1.getCircumference();
// Print the radius, area and circumference of the two circles.
   System.out.println("Circle: r = " + circleRadius + ", area = " + circleArea + ", circumference = " + circleCircumference);
   System.out.println("C1: r = " + c1Radius + ", area = " + c1Area + ", circumference = " + c1Circumference);
  }
}

public class Circle // extends Object
{
    // instance variables
    private int radius;


    // constructors
    public Circle()
    {
        radius = 1;
    }


    public Circle(int r)
    {
        radius = r;
    }


    // accessor method (AKA getter)
    public int getRadius()
    {
        return radius;
    }
    // returns the area of this circle
    public double getArea()
    {
        return Math.PI * radius * radius;
    }
    //Write your own method below that returns the circumference of this circle
    public double getCircumference()
    {
        return 2 * Math.PI * radius;
    }
}

import java.io.*;
import java.util.*;

class Point {
    String name;
    double x, y;

    Point(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return name + ": (" + x + ", " + y + ")";
    }
}

public class ReadingCoords {

    // Method to read points from a file
    public static List<Point> readPointsFromFile(String fileName) {
        List<Point> points = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String name = data[0].trim();
                double x = Double.parseDouble(data[1].trim());
                double y = Double.parseDouble(data[2].trim());
                points.add(new Point(name, x, y));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found.");
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return points;
    }

    // Method to calculate the distance between two points
    public static double calculateDistance(Point p1, Point p2) {
        double distance = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
        return Math.round(distance * 100.0) / 100.0; // Round to 2 decimal places
    }

    public static void main(String[] args) {
        String fileName = "coordinates.txt"; // Replace with your file name
        List<Point> points = readPointsFromFile(fileName);

        if (points.size() != 3) {
            System.err.println("Error: File must contain exactly 3 points.");
            return;
        }

        // Print coordinates
        System.out.println("Triangle Points:");
        for (Point point : points) {
            System.out.println(point);
        }

        // Calculate and print distances
        double ab = calculateDistance(points.get(0), points.get(1));
        double ac = calculateDistance(points.get(0), points.get(2));
        double bc = calculateDistance(points.get(1), points.get(2));

        System.out.println("\nDistances:");
        System.out.println(points.get(0).name + points.get(1).name + ": " + ab);
        System.out.println(points.get(0).name + points.get(2).name + ": " + ac);
        System.out.println(points.get(1).name + points.get(2).name + ": " + bc);
    }
}

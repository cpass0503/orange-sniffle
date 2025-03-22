import java.io.*;
import java.util.*;


public class TriangleCoordinates {
    public void readFile() {
        String fileName = "coordinates.txt";
        String[] pointNames = new String[3];
        Double[] xcoords = new Double[3];
        Double[] ycoords = new Double[3];

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int index = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                
                if (parts.length != 3) {
                    System.out.println("Formatting error: Line does not contain exactly 3 parts separated by ':' -> " + line);
                    continue;
                }

                try {
                    pointNames[index] = parts[0].trim();
                    xcoords[index] = Double.parseDouble(parts[1].trim());
                    ycoords[index] = Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Formatting error: Coordinates are not valid numbers -> " + line);
                    continue;
                }

                System.out.println("Point " + pointNames[index] + ": (" + xcoords[index] + "," + ycoords[index] + ")");
                index++;
                
                if (index == 3) break; // Stop reading after 3 valid points
            }

            br.close();

            if (index == 3) {
                printDistances(pointNames, xcoords, ycoords);
            } else {
                System.out.println("Invalid number of points in file. At least 3 valid points are required.");
            }
        } catch (FileNotFoundException error) {
            System.out.println("File not found: " + fileName);
        } catch (IOException error) {
            System.out.println("An error occurred while reading the file.");
            error.printStackTrace();
        }
    }

    public double getDistance(double x1, double x2, double y1, double y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    private void printDistances(String[] pointNames, Double[] xcoords, Double[] ycoords) {
        double distanceAB = getDistance(xcoords[0], xcoords[1], ycoords[0], ycoords[1]);
        double distanceBC = getDistance(xcoords[1], xcoords[2], ycoords[1], ycoords[2]);
        double distanceCA = getDistance(xcoords[2], xcoords[0], ycoords[2], ycoords[0]);

        System.out.printf("%s%s: %.2f%n", pointNames[0], pointNames[1], distanceAB);
        System.out.printf("%s%s: %.2f%n", pointNames[1], pointNames[2], distanceBC);
        System.out.printf("%s%s: %.2f%n", pointNames[2], pointNames[0], distanceCA);
    }

    public static void main(String[] args) {
        TriangleCoordinates program = new TriangleCoordinates();
        program.readFile();
    }
}

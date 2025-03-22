import java.util.Scanner;
import java.util.Arrays;

public class EquationParser {
    public static int[] parseEquation(String eq) {
        eq = eq.replace(" ", "");
        int[] temp = new int[eq.length()];
        int index = 0;
        int i = 0;
        while (i < eq.length()) {
            int sign = 1;
            char ch = eq.charAt(i);
            if (ch == '+' || ch == '-') {
                if (ch == '-') {
                    sign = -1;
                } else {
                    sign = 1;
                }
                i++;
            }
            int num = 0;
            int start = i;
            while (i < eq.length() && Character.isDigit(eq.charAt(i))) {
                num = num * 10 + (eq.charAt(i) - '0');
                i++;
            }
            if (i > start) {
                temp[index] = sign * num;
                index++;
                if (i < eq.length() && Character.isLetter(eq.charAt(i))) {
                    i++;
                }
            } else if (i < eq.length() && Character.isLetter(eq.charAt(i))) {
                temp[index] = sign;
                index++;
                i++;
            } else {
                i++;
            }
        }
        int[] result = new int[index];
        for (int j = 0; j < index; j++) {
            result[j] = temp[j];
        }
        return result;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the first equation (e.g., 7x - 3y = 212):");
        String equation1 = scanner.nextLine();

        System.out.println("Enter the second equation (e.g., 8x + y = 20):");
        String equation2 = scanner.nextLine();

        int[] eq1 = parseEquation(equation1);
        System.out.println("Equation 1 coefficients: " + Arrays.toString(eq1));

        int[] eq2 = parseEquation(equation2);
        System.out.println("Equation 2 coefficients: " + Arrays.toString(eq2));

        if (eq1.length < 3 || eq2.length < 3) {
            System.out.println("Error: One or both equations are not in the expected format.");
            scanner.close();
            return;
        }
        int[][] D = { {eq1[0], eq1[1]}, {eq2[0], eq2[1]} };
        int DDeterminant = (D[0][0] * D[1][1]) - (D[0][1] * D[1][0]);

        int[][] Dx = { {eq1[2], eq1[1]}, {eq2[2], eq2[1]} };
        int DxDeterminant = (Dx[0][0] * Dx[1][1]) - (Dx[0][1] * Dx[1][0]);

        int[][] Dy = { {eq1[0], eq1[2]}, {eq2[0], eq2[2]} };
        int DyDeterminant = (Dy[0][0] * Dy[1][1]) - (Dy[0][1] * Dy[1][0]);
        
        if (DDeterminant == 0) {
            if (DxDeterminant == 0 && DyDeterminant == 0) {
                System.out.println("The system has infinitely many solutions.");
            } else {
                System.out.println("The system has no solution.");
            }
        } else {
            double xVal = (double) DxDeterminant / DDeterminant;
            double yVal = (double) DyDeterminant / DDeterminant;
            System.out.println("x: " + xVal);
            System.out.println("y: " + yVal);
        }
        scanner.close();
    }
}

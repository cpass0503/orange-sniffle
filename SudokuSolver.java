public class SudokuSolver {

    public static boolean isValid(int[][] grid, int r, int c, int k) {
        // Check the row
        for (int i = 0; i < 9; i++) {
            if (grid[r][i] == k) {
                return false;
            }
        }

        // Check the column
        for (int i = 0; i < 9; i++) {
            if (grid[i][c] == k) {
                return false;
            }
        }

        // Check the 3x3 box
        int boxRowStart = (r / 3) * 3;
        int boxColStart = (c / 3) * 3;
        for (int i = boxRowStart; i < boxRowStart + 3; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                if (grid[i][j] == k) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean solve(int[][] grid, int r, int c) {
        if (r == 9) {
            return true; // Solved the grid
        }

        if (c == 9) {
            return solve(grid, r + 1, 0); // Move to the next row
        }

        if (grid[r][c] != 0) {
            return solve(grid, r, c + 1); // Skip already filled cells
        }

        for (int k = 1; k <= 9; k++) {
            if (isValid(grid, r, c, k)) {
                grid[r][c] = k; // Place the number
                if (solve(grid, r, c + 1)) {
                    return true; // If solved, return true
                }
                grid[r][c] = 0; // Backtrack
            }
        }

        return false; // No solution found
    }

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 0, 0, 5, 0, 0, 0, 6},
            {0, 0, 0, 0, 0, 1, 4, 0, 0},
            {0, 1, 2, 0, 0, 0, 0, 0, 9},
            {8, 0, 0, 0, 0, 0, 0, 0, 0},
            {7, 0, 0, 0, 0, 6, 0, 5, 0},
            {0, 4, 0, 0, 0, 0, 8, 7, 0},
            {0, 0, 3, 4, 0, 0, 0, 0, 0},
            {9, 0, 4, 6, 0, 0, 0, 0, 0},
            {0, 0, 0, 7, 8, 5, 0, 0, 0}
        };

        if (solve(grid, 0, 0)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    System.out.print(grid[i][j] + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("No solution exists.");
        }
    }
}

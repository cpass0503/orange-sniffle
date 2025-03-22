from Sudoku.extractor import extract_grid
from Sudoku.solver import solve, is_valid_grid

def print_grid(grid):
    for row in grid:
        print(" ".join(str(num) if num != 0 else "." for num in row))

if __name__ == "__main__":
    image_path = "board.png"

    print("Extracting Sudoku grid...")
    grid = extract_grid(image_path)

    # Debugging: Print and save the extracted grid
    print("\nExtracted Sudoku Grid:")
    print_grid(grid)

    # Save the extracted grid to a text file
    with open("extracted_grid.txt", "w") as f:
        for row in grid:
            f.write(" ".join(str(num) if num != 0 else "." for num in row) + "\n")

    # Validate the grid and attempt to solve
    if is_valid_grid(grid):
        print("\nSolving Sudoku...")
        if solve(grid):
            print("Solved Sudoku Grid:")
            print_grid(grid)
        else:
            print("No solution exists for the extracted grid.")
    else:
        print("The extracted grid is invalid and cannot be solved.")

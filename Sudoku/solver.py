def is_valid(grid, r, c, k):
    # Check row
    if k in grid[r]:
        return False

    # Check column
    if k in [grid[i][c] for i in range(9)]:
        return False

    # Check 3x3 box
    box_row, box_col = r // 3 * 3, c // 3 * 3
    for i in range(box_row, box_row + 3):
        for j in range(box_col, box_col + 3):
            if grid[i][j] == k:
                return False

    return True


def is_valid_grid(grid):
    # Validate the entire grid before solving
    for r in range(9):
        for c in range(9):
            num = grid[r][c]
            if num != 0:
                grid[r][c] = 0  # Temporarily remove the number
                if not is_valid(grid, r, c, num):
                    return False
                grid[r][c] = num  # Restore the number
    return True


def solve(grid, r=0, c=0):
    if r == 9:
        return True  # Successfully solved
    if c == 9:
        return solve(grid, r + 1, 0)
    if grid[r][c] != 0:
        return solve(grid, r, c + 1)

    for k in range(1, 10):
        if is_valid(grid, r, c, k):
            grid[r][c] = k
            if solve(grid, r, c + 1):
                return True
            grid[r][c] = 0  # Backtrack

    return False  # No solution found


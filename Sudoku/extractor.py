import cv2
import numpy as np
import pytesseract
import matplotlib.pyplot as plt  # Import matplotlib for inline debugging

def preprocess_image(image_path):
    # Load the image in grayscale
    image = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)

    # Apply GaussianBlur to reduce noise
    blurred = cv2.GaussianBlur(image, (5, 5), 0)

    # Apply adaptive thresholding to segment the grid
    thresh = cv2.adaptiveThreshold(blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                   cv2.THRESH_BINARY_INV, 11, 2)

    return thresh


def extract_grid(image_path):
    # Preprocess the image
    thresh = preprocess_image(image_path)

    # Remove grid lines
    cleaned = remove_grid_lines(thresh)

    # Save the cleaned grid for debugging
    cv2.imwrite("updated_cleaned_grid.png", cleaned)

    # Find contours to locate the Sudoku grid
    contours, _ = cv2.findContours(cleaned, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    if len(contours) == 0:
        cv2.imwrite("debug_no_contours.png", cleaned)
        raise ValueError("No contours found. Check the grid preprocessing steps.")

    # Debug: Save all detected contours
    debug_contours = cleaned.copy()
    cv2.drawContours(debug_contours, contours, -1, 255, 2)
    cv2.imwrite("debug_contours.png", debug_contours)

    # Save individual contours for debugging
    for idx, contour in enumerate(contours):
        contour_image = np.zeros_like(cleaned)
        cv2.drawContours(contour_image, [contour], -1, 255, thickness=cv2.FILLED)
        cv2.imwrite(f"debug_contour_{idx}.png", contour_image)

    # Filter contours by size
    min_area = 0.1 * (cleaned.shape[0] * cleaned.shape[1])  # Lower threshold to 10%
    filtered_contours = [contour for contour in contours if cv2.contourArea(contour) > min_area]
    if len(filtered_contours) == 0:
        cv2.imwrite("debug_failed_contours.png", debug_contours)
        raise ValueError("No valid contours found. Ensure the grid is clear and prominent.")

    # Use the largest valid contour
    largest_contour = max(filtered_contours, key=cv2.contourArea)

    # Approximate the contour to a rectangle
    epsilon = 0.02 * cv2.arcLength(largest_contour, True)
    approx = cv2.approxPolyDP(largest_contour, epsilon, True)

    # Debug: Save largest contour and approximation
    if len(approx) != 4:
        debug_grid = cleaned.copy()
        cv2.drawContours(debug_grid, [largest_contour], -1, 255, 3)
        cv2.imwrite("debug_largest_contour.png", debug_grid)
        raise ValueError(f"Failed to approximate grid to 4 points. Found {len(approx)} points.")

    # Warp the grid into a top-down perspective
    points = np.array([point[0] for point in approx], dtype="float32")
    points = points[np.argsort(points[:, 0])]
    top_left, bottom_left = sorted(points[:2], key=lambda x: x[1])
    top_right, bottom_right = sorted(points[2:], key=lambda x: x[1])

    grid_corners = np.array([top_left, top_right, bottom_right, bottom_left], dtype="float32")
    size = 450  # Grid size (450x450)
    destination = np.array([[0, 0], [size - 1, 0], [size - 1, size - 1], [0, size - 1]], dtype="float32")

    M = cv2.getPerspectiveTransform(grid_corners, destination)
    warped = cv2.warpPerspective(cleaned, M, (size, size))

    # Debugging: Save the warped grid
    cv2.imwrite("debug_warped.png", warped)

    # Split the grid into 9x9 cells
    if warped.shape[0] < 9 or warped.shape[1] < 9:
        raise ValueError("Warped grid is too small or invalid.")

    grid = np.zeros((9, 9), dtype=int)
    cell_size = warped.shape[0] // 9
    margin = 2  # Add a small margin for cropping

    for i in range(9):
        for j in range(9):
            # Crop each cell with a margin
            cell = warped[max(0, i * cell_size - margin):min((i + 1) * cell_size + margin, warped.shape[0]),
                          max(0, j * cell_size - margin):min((j + 1) * cell_size + margin, warped.shape[1])]

            # Resize and threshold for better OCR
            cell = cv2.resize(cell, (56, 56))
            cell = cv2.threshold(cell, 128, 255, cv2.THRESH_BINARY)[1]
            cell = cv2.normalize(cell, None, 0, 255, cv2.NORM_MINMAX)

            # Perform OCR
            digit = pytesseract.image_to_string(
                cell, 
                config="--psm 10 -c tessedit_char_whitelist=0123456789"
            ).strip()

            # Validate OCR result
            if digit.isdigit() and len(digit) == 1:
                grid[i][j] = int(digit)
            else:
                grid[i][j] = 0

    return grid





def remove_grid_lines(image):
    # Define kernels for horizontal and vertical line removal
    horizontal_kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (50, 1))
    vertical_kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (1, 50))

    # Remove horizontal lines
    horizontal_lines = cv2.morphologyEx(image, cv2.MORPH_OPEN, horizontal_kernel)
    image_no_horizontal = cv2.subtract(image, horizontal_lines)

    # Remove vertical lines
    vertical_lines = cv2.morphologyEx(image_no_horizontal, cv2.MORPH_OPEN, vertical_kernel)
    image_no_lines = cv2.subtract(image_no_horizontal, vertical_lines)

    # Additional dilation/erosion to smooth the cleaned grid
    kernel = np.ones((3, 3), np.uint8)
    cleaned = cv2.dilate(image_no_lines, kernel, iterations=1)
    cleaned = cv2.erode(cleaned, kernel, iterations=1)

    return cleaned


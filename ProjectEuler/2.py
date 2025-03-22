# Initialize the first two terms of the Fibonacci sequence
a, b = 1, 2

# Initialize the sum of even-valued terms
sum_even = 0

# Loop until the Fibonacci term exceeds 4 million
while a <= 4000000:
    # Add to the sum if the term is even
    if a % 2 == 0:
        sum_even += a
    # Move to the next term in the Fibonacci sequence
    a, b = b, a + b

# Print the result
print("The sum of even-valued Fibonacci terms not exceeding four million is:", sum_even)

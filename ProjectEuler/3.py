# Function to find the largest prime factor
def largest_prime_factor(n):
    factor = 2  # Start with the smallest prime number
    while factor * factor <= n:  # Only check up to the square root of n
        if n % factor == 0:
            n //= factor  # Divide n by the factor if it's a factor
        else:
            factor += 1  # Move to the next potential factor
    return n  # The remaining n is the largest prime factor

# Define the number
number = 600851475143

# Find and print the largest prime factor
result = largest_prime_factor(number)
print("The largest prime factor of 600851475143 is:", result)

# Define the limit
limit = 1000

# Calculate the sum of all multiples of 3 or 5 below the limit
sum_of_multiples = sum(x for x in range(limit) if x % 3 == 0 or x % 5 == 0)

# Print the result
print("The sum of all multiples of 3 or 5 below 1000 is:", sum_of_multiples)

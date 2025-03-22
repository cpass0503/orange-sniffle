/*
 * This program demonstrates polymorphism in Java.
 * It defines a Vehicle superclass with a move() method,
 * and two subclasses, Car and Bike, that override the move() method.
 * An array of Vehicle objects is created, containing both Car and Bike instances.
 * When the move() method is called on each object, the actual object's type
 * determines which method is executed, demonstrating runtime polymorphism.
 */

 class Vehicle {
    public void move() {
        System.out.println("The vehicle is moving.");
    }
}

class Car extends Vehicle {
    @Override
    public void move() {
        System.out.println("The car is driving on the highway.");
    }
}

class Bike extends Vehicle {
    @Override
    public void move() {
        System.out.println("The bike is cruising through the park.");
    }
}

public class Polymorph {
    public static void main(String[] args) {
        // Create an array of Vehicle objects with Car and Bike instances
        Vehicle[] vehicles = { new Car(), new Bike(), new Car(), new Bike() };

        System.out.println("Polymorphism demonstration with Vehicles:");
        for (Vehicle vehicle : vehicles) {
            vehicle.move(); // The overridden method is called depending on the actual object type.
        }
    }
}

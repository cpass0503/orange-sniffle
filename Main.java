// Superclass
class Person {
    String name;
    int age;

    // Constructor for Person
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Method in the superclass
    public void introduce() {
        System.out.println("Hi, I'm " + name + ", I'm " + age + " years old");
    }
}

// Subclass that inherits from Person
class Empluzz extends Person {
    String school;
    String job;

    // Constructor for Student
    public Empluzz(String name, int age, String job) {
        super(name, age);  // Calling the constructor of the superclass
        this.job = job;
    }

    // Overriding the introduce method
    @Override
    public void introduce() {
        super.introduce();  // Call the superclass method
        System.out.println("and I have a job unlike Walter. I am a(n) " + job);
    }
}
    // Subclass that inherits from Person
class Unempluzz extends Person {
    double brainrotLevel;
    double screenTimeHours;

    // Constructor for Student
    public Unempluzz(String name, int age, double brainrotLevel, double screenTimeHours) {
        super(name, age);  // Calling the constructor of the superclass
        this.brainrotLevel = brainrotLevel;
        this.screenTimeHours = screenTimeHours;
    }

    // Overriding the introduce method
    @Override
    public void introduce() {
        super.introduce();  // Call the superclass method
        System.out.println("and like Walter, I am unemployed. " + (brainrotLevel*100) + " % of my brain is rotted and i spend " + screenTimeHours + " hours a day doomscrolling.");
    }
}

public class Main {
    public static void main(String[] args) {
        // Create an object of the Student class
        Empluzz henry = new Empluzz("Henry", 18, "Popcorn Bagger");
        Unempluzz walter = new Unempluzz("Walter", 18, 1.01, 25);

        // Call the introduce method, which is overridden in the Student class
        henry.introduce();
        walter.introduce();
    }
}

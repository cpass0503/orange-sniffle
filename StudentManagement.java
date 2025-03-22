import java.util.ArrayList;
import java.util.Scanner;

public class StudentManagement {
    static ArrayList<String> students = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add a Student");
            System.out.println("2. Remove a Student");
            System.out.println("3. Display Students");
            System.out.println("4. Search for a Student");
            System.out.println("5. Update a Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    addStudent();
                }
                case 2 -> {
                    removeStudent();
                }
                case 3 -> {
                    displayStudents();
                }
                case 4 -> {
                    searchStudents();
                }
                case 5 -> {
                    updateStudent();
                }
                case 6 -> System.out.println("Exiting the program.");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        scanner.close();
    }
    public static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        students.add(name);
        System.out.println("Student added: " + name);
    }

    public static void removeStudent() {
        System.out.print("Enter the name or index (0-" + (students.size() - 1) +") of the student you would like to remove: ");
        String nameOrIndex = scanner.nextLine();
        boolean removed = false;
        try {
            int index = Integer.parseInt(nameOrIndex);
            if (index >= 0 && index < students.size()) {
                System.out.println("Removing student: " + students.get(index));
                students.remove(index);
                removed = true;
            } else {
                System.out.println("Invalid index. No student removed.");
                removed = false;
            }
        } catch (NumberFormatException e) {
            for (String student : students) {
                if (student.equalsIgnoreCase(nameOrIndex)) {
                    students.remove(student);
                    System.out.println("Removed student: " + student);
                    removed = true;
                    break;
                } 
            }
            if (removed == false) {
                System.out.println("Student not found, no one removed.");
            }
        }
    }

    public static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the list.");
        } else {
            System.out.println("Student List:");
            for (int i = 0; i < students.size(); i++) {
                System.out.println(i + ": " + students.get(i));
            }
        }
    }

    public static void searchStudents()
    {
        System.out.print("Enter the name of the student: ");
        String name = scanner.nextLine();
        boolean contains = false;
        for (String student : students) {
            if (name.equalsIgnoreCase(student))
            {
                System.out.println("The student list contains "+name);
                contains = true;
                break;
            }
        }
        if (!contains)
            System.out.println("The student list does not contain " + name);
    }




    public static void updateStudent()
    {
        System.out.print("Enter the name or index of the student you would like to update: ");
        String nameOrIndex = scanner.nextLine();
        System.out.print("Enter the updated name: ");
        String NewName = scanner.nextLine();
        boolean updated = false;
        try {
            int index = Integer.parseInt(nameOrIndex);
            if (index >= 0 && index < students.size()) {
                System.out.println("Updating student name " + students.get(index) + " to " + NewName);
                students.set(index, NewName);
                updated = true;
            } else {
                System.out.println("Invalid index. Update failed.");
                updated = false;
            }
        } catch (NumberFormatException e) {
            for (String student : students) {
                if (student.equalsIgnoreCase(nameOrIndex)) {
                    int index = students.indexOf(student);
                    System.out.println("Updating student name " + students.get(index) + " to " + NewName);
                    students.set(index, NewName);
                    updated = true;
                    break;
                }
            }
        if (updated == false) {
            System.out.println("Student not found, update failed.");
        }
    }
}

}

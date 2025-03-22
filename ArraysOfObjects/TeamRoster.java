import java.io.*;

public class TeamRoster {
    // instance variables for our objects
    private String name;
    private String position;
    private int grade;

    // constructor
    public TeamRoster(String position, String name, int grade) {
        // when our passed in variable names match the instance variable names, we have to use this.(variable)
        this.name = name;
        this.position = position;
        this.grade = grade;
    }

    // We are passing in an array of players... every player is "made" by the constructor
        // that means every element is type TeamRoster... because TeamRoster is the constructor
    public static void readFile(TeamRoster[] roster) {
        int index = 0;
        try(BufferedReader br = new BufferedReader(new FileReader("teamRoster.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String name = parts[0] + " " + parts[1];
                // we HAVE to use "new" because we are making a new instance of TeamRoster
                roster[index] = new TeamRoster(parts[2], name, Integer.parseInt(parts[3]));
                // each player in the roster is "made" using the constructor
                // the constructor is formatted: TeamRoster(String position, String name, int grade)
                    // so, we have to pass in the variables in that order
                index++;
            }
        }
        catch (IOException e) {
            System.out.println("Invalid file name");
        }
        catch(NumberFormatException e) {
            System.out.println("invalid grade on line: " + (index+1));
        }
    }

    public static void printPosition(TeamRoster[] roster) {
        // sort by position
        String[] positions = {"Attack", "Midfield", "Defense", "Goalie"};
        for (String pos : positions) {
            System.out.println("Players that play " + pos + " are: ");
            for (TeamRoster player : roster)
            {
                // player.position is the position each player plays
                // pos is the name of the position we are viewing in the array of positions
                if (player.position.equals(pos)) {
                    System.out.print(player.name + ", ");
                }
            }
            System.out.println();
        }
    }
    public static void printGrade(TeamRoster[] roster) {
        //sort by grade
        int[] grades = {12, 11, 10, 9, 8};
        for (int grade0 : grades) {
            System.out.println("Players in " + grade0 + "th grade are: ");
            for (TeamRoster player : roster)
            {
                // player.position is the position each player plays
                // pos is the name of the position we are viewing in the array of positions
                if (player.grade == grade0) {
                    System.out.print(player.name + ", ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // create the array of objects
            // when making an array of integers: int[] name = new int[# of elements]
                // int[] is an array made up of integers
            // IN THIS PROGRAM, every element is a different player
                // the constructor "makes" each player
                // so the array is type TeamRoster (name of the constructor)

        TeamRoster[] roster = new TeamRoster[29];
        // TeamRoster is the type of element, just how int is a type, or string
        // so, an array of that type will be TeamRoster[] instead of int[] or string[]

        // read the file, pass in the array of objects that we will populate
        readFile(roster);
        // arrange the roster by position and print
        //printPosition(roster);
        printGrade(roster);



        // arrange the roster by position

        // Use constructor to "make" each player
    }
}
import java.util.Scanner;

public class Riddle {
    private String question;
    private String answer;

    public Riddle()
    {
        int questionNumber = (int) (3 * Math.random());
        if (questionNumber == 0) {
            question = "Why did the chicken cross the road?";
            answer = "To get to the other side";
        } else if (questionNumber == 1) {
            question = "What is so delicate that saying its name breaks it?";
            answer = "Silence";
        } else {
            question = "What belongs to you, but others use it more than you do?";
            answer = "Your name";
        }
    }

    public void printQuestion()
    {
        System.out.println(question);

    }
    public void printAnswer()
    {
        System.out.println(answer);
    }
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        Riddle riddleQuestion = new Riddle();
        // riddleQuestion is an object of the Riddle class.
            // the instance variables of the riddle class are:
                // String question
                // String answer
        // riddleQuestion has 2 attributes: question and answer
        riddleQuestion.printQuestion();
        // we wrote the printQuestion method to print the question
            // so calling the method, will print the question attribute of riddleQuest
        System.out.println("Would you like to see the answer?");
        scan.nextLine();
        riddleQuestion.printAnswer();

    }

}

import java.io.*;


public class HockeyStats {
   private String name;
   private int gamesPlayed;
   private int goals;
   private int assists;
   private int points;


   // constructor
   public HockeyStats (String name, int gamesPlayed, int goals, int assists) {
       this.name = name;
       this.gamesPlayed = gamesPlayed;
       this.goals = goals;
       this.assists = assists;
       points = goals+assists;
   }


   public static void readFile(HockeyStats[] stats) {
       int index = 0;
       try(BufferedReader br = new BufferedReader(new FileReader("playerStats.txt"))) {
           String line;
           while ((line = br.readLine()) != null) {
               String[] parts = line.split(":");
               String name = parts[0];
               String[] pieces = line.split(" ");
               int gamesPlayed = Integer.parseInt(pieces[2]);
               int goals = Integer.parseInt(pieces[3]);
               int assists = Integer.parseInt(pieces[4]);


               // we HAVE to use "new" because we are making a new instance of HockeyStats
               stats[index] = new HockeyStats(name, gamesPlayed, goals, assists);
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


   public static int greatestInt(int[] nums) {
       int max = nums[0];
       for (int number : nums)
       {
           if (number > max)
           {
               max = number;
           }
       }
       return max;
   }


   public static void mostAssists (HockeyStats[] stats) {
       int[] nums = new int[20];
       for (int i = 0; i < nums.length; i++)
       {
           nums[i] = stats[i].assists;
       }
       int max = greatestInt(nums);
       System.out.println("The player(s) with the most assists with " + max + " is:");
       for (int n = 0; n < nums.length; n++)
       {
           if (stats[n].assists == max)
           {
               System.out.println(stats[n].name);
           }
       }
   }


   public static void mostGames (HockeyStats[] stats) {
       int[] nums = new int[20];
       for (int i = 0; i < nums.length; i++)
       {
           nums[i] = stats[i].gamesPlayed;
       }
       int max = greatestInt(nums);
       System.out.println("The player(s) with the most games played with " + max + " is:");
       for (int n = 0; n < nums.length; n++)
       {
           if (stats[n].gamesPlayed == max)
           {
               System.out.println(stats[n].name);
           }
       }
   }


   public static void mostGoals (HockeyStats[] stats) {
       int[] nums = new int[20];
       for (int i = 0; i < nums.length; i++)
       {
           nums[i] = stats[i].goals;
       }
       int max = greatestInt(nums);
       System.out.println("The player(s) with the most goals with " + max + " is:");
       for (int n = 0; n < nums.length; n++)
       {
           if (stats[n].goals == max)
           {
               System.out.println(stats[n].name);
           }
       }
   }


   public static void mostPoints (HockeyStats[] stats) {
       int[] nums = new int[20];
       for (int i = 0; i < nums.length; i++)
       {
           nums[i] = stats[i].points;
       }
       int max = greatestInt(nums);
       System.out.println("The player(s) with the most points with " + max + " is:");
       for (int n = 0; n < nums.length; n++)
       {
           if (stats[n].points == max)
           {
               System.out.println(stats[n].name);
           }
       }
   }


   public static void main(String[] args) {
       HockeyStats[] stats = new HockeyStats[20];
       readFile(stats);
       mostGames(stats);
       System.out.println();
       mostGoals(stats);
       System.out.println();
       mostAssists(stats);
       System.out.println();
       mostPoints(stats);
   }
}








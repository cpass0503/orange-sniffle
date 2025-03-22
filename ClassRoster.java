public class ClassRoster {
    public String lastName(String[] roster) {
        String current = roster[0];
        for (int i = 1; i < roster.length; i++) {
            if (current.compareTo(roster[i]) < 0) {
                current = roster[i];
            } else {
                continue;
            }
            
        }
        return current;
    }
    public String randomName(String[] roster) {
        return roster[(int) (roster.length * Math.random())];
    }

    public static void main(String[] args) {
        String[] names= {"Mike", "Ilan", "Shannon", "Katie", "Kate", "Mike", "Sophia"};

        ClassRoster cr = new ClassRoster();

        System.out.println("Last name on roster: " + cr.lastName(names) + ". Random name on roster: " + cr.randomName(names));
    }
}
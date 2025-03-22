public class strings3 {
    public static void main(String[] args)
    {
        String name = "Johnny Chimpo";
        int length = name.length();
        int position = 3;
        char a = name.charAt(position);
        String b = name.substring(4, 10);
        String c = b.substring(1);
        int index = name.indexOf("Chimp");
        String statement;
        if (name.compareTo("Officer Farva") < 0)
        {
            statement = "Meow";
        }
        else
        {
            statement = "Ram Rod";
        }
        System.out.println("length is " + length);
        System.out.println("Character at index " + position + " is " + a);
        System.out.println("String b is " + b);
        System.out.println("String c is " + c);
        System.out.println("statement " + statement);
    }
}

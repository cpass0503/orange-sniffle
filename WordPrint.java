public class WordPrint {
    public int averager(String[] strings) {
        int totalStrings = strings.length;
        int totalCharacters = 0;
        for (String string : strings) {
            totalCharacters += string.length();
        }
        int average = Math.round((float) totalCharacters/totalStrings);
        return average;
    }
    public void shortener(String[] strings, int n) {
        for (String string : strings) {
            if (string.length() <= n) {
                System.out.println(string + " ");
            } else {
                System.out.println(string.substring(0, n));
            }
        }
    }
    public static void main(String[] args) {
        String[] words = {"apples", "bananas", "rizz", "pizza", "beach", "mountains" };


        WordPrint wp = new WordPrint();

        int n = wp.averager(words);
        wp.shortener(words, n);
    }
}

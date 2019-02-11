/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {
    public static void main(String[] args) {
        //defaultTest();
        int maxN = mostPalindromesN();
        printPalindromeN(maxN);
    }

    public static void defaultTest() {
        int minLength = 4;
        In in = new In("../library-sp19/data/words.txt");
        Palindrome palindrome = new Palindrome();
        OffByN obnComparator = new OffByN(5);

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word, obnComparator)) {
                System.out.println(word);
            }
        }
    }

    /**
     * Finds the N for which there are the most palindromes in English
     * This method assumes the words are all lowercase |a - z| = 25
     * @return
     */
    public static int mostPalindromesN() {
        // variables to use
        In in;
        Palindrome palindrome = new Palindrome();
        OffByN obnComparator;
        String longestp;
        int maxN = 0;
        int maxCount = 0;
        int count;

        System.out.println("----------- Palindrome Analyzer --------------");

        // this is very inefficient but I am lazy right now.
        for (int n = 0; n < 25; n++) { // |a - z| == 25
            in = new In("../library-sp19/data/words.txt"); // reload
            obnComparator = new OffByN(n);
            longestp = "";
            count = 0;

            // check how much palindromes there are at N = n
            while (!in.isEmpty()) {
                String word = in.readString();
                if (palindrome.isPalindrome(word, obnComparator)) {
                    count++;
                    if (word.length() > longestp.length()) {
                        longestp = word;
                    }
                }
            }

            if (count > maxCount) {
                maxCount = count;
                maxN = n;
            }

            System.out.println("Longest for N = " + n + " out of " + count + " is: " + longestp);
        }
        System.out.println();
        System.out.println("The N with most palindromes is: " + maxN);
        System.out.println();
        return maxN;
    }

    public static void printPalindromeN(int N) {
        System.out.println("----------- Palindrome Printer N = " + N + " --------------");
        In in = new In("../library-sp19/data/words.txt");
        Palindrome palindrome = new Palindrome();
        OffByN obnComparator = new OffByN(N);

        while (!in.isEmpty()) {
            String word = in.readString();
            if (palindrome.isPalindrome(word, obnComparator)) {
                System.out.println(word);
            }
        }
    }
}

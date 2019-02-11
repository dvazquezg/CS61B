/**
 * Palindrome class provide methods to check if a word is a palindrome.
 * @author Daniel Vazquez
 */
public class Palindrome {
    /**
     * Creates a deque containing the characters of the given word
     * @param word tp be converted
     * @return the word as deque
     */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new LinkedListDeque<Character>();
        for (int i = 0; i < word.length(); i++) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    /**
     * Checks is the given world is a palindrome
     * @param word to be analyzed
     * @return true of word is palindrome, false otherwise
     */
    public boolean isPalindrome(String word) {
        Deque<Character> dword = this.wordToDeque(word);
        for (int i = 0; i < word.length() / 2; i++) {
            if (!dword.removeFirst().equals(dword.removeLast())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the word is palindrome
     * @param word to be analyzed
     * @param cc the comparator interface to be used
     * @return the result of the analysis
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> dword = this.wordToDeque(word);
        for (int i = 0; i < word.length() / 2; i++) {
            if (!cc.equalChars(dword.removeFirst(), dword.removeLast())) {
                return false;
            }
        }
        return true;
    }
}

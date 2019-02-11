import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        OffByOne oboComparator = new OffByOne();
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));
        assertFalse(palindrome.isPalindrome("universe"));
        assertTrue(palindrome.isPalindrome("kayak"));
        assertFalse(palindrome.isPalindrome("kAyak"));
        assertTrue(palindrome.isPalindrome("RACECAR"));
        assertTrue(palindrome.isPalindrome("noon"));
        assertFalse(palindrome.isPalindrome("daniel"));
        assertFalse(palindrome.isPalindrome("aaahaaaa"));
        assertTrue(palindrome.isPalindrome("flake", oboComparator));
        assertFalse(palindrome.isPalindrome("Flake", oboComparator));
        assertTrue(palindrome.isPalindrome("$01#", oboComparator));
        assertFalse(palindrome.isPalindrome("$%!%$", oboComparator));
        assertTrue(palindrome.isPalindrome("", oboComparator));
        assertTrue(palindrome.isPalindrome("a", oboComparator));
        assertTrue(palindrome.isPalindrome("*", oboComparator));
        assertFalse(palindrome.isPalindrome("NOON", oboComparator));
        assertFalse(palindrome.isPalindrome("DANIEL", oboComparator));
        assertTrue(palindrome.isPalindrome("BSTA", oboComparator));
        assertTrue(palindrome.isPalindrome("FLAKE", oboComparator));
    }
}

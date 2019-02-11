public class OffByOne implements CharacterComparator {

    /**
     * Returns true if characters exactly off by one character
     * @param x first character
     * @param y second character
     * @return result of comparison
     */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1 ? true : false;
    }
}

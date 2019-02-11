public class OffByN implements CharacterComparator {
    private int N;
    /**
     * Creates a new OffByN object
     * @param N the character offset
     */
    public OffByN(int N) {
        this.N = N;
    }

    /**
     * Returns true if characters exactly off by N characters
     * @param x first character
     * @param y second character
     * @return result of comparison
     */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N ? true : false;
    }
}

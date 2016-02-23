package ru.spbau.mit;


public final class CharToIntConverter {

    public static final int Z_INDEX = 91;
    public static final int BIG_A_INDEX = 65;
    public static final int SMALL_A_INDEX = 97;
    public static final int ALPHABET_SIZE = 26;

    private CharToIntConverter() {
    }

    public static int charToInt(char symb) {
        int index = (int) symb;
        if (index < Z_INDEX) {
            return index - BIG_A_INDEX;
        } else {
            return index - SMALL_A_INDEX + ALPHABET_SIZE;
        }
    }

}

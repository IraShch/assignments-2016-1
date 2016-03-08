package ru.spbau.mit;


public final class CharToIntConverter {

    private CharToIntConverter() {
    }

    public static int charToInt(char symb) {
        int index = (int) symb;
        if (index < 'z') {
            return index - 'A';
        } else {
            return index - 'a' + ('z' - 'a' + 1);
        }
    }

}

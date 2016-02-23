package ru.spbau.mit;

/**
 * Created by Ira on 22/02/16.
 */
public class CharToIntConverter {
    public static int charToInt(char symb) {
        int index = (int) symb;
        if (index < 91) {
            return index - 65;
        } else {
            return index - 97 + 26;
        }
    }

}

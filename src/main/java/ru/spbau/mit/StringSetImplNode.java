package ru.spbau.mit;


import static ru.spbau.mit.CharToIntConverter.charToInt;


class StringSetImplNode {

    private static final int ALPHABET_SIZE = 26;

    private boolean isTerminal = false;
    private StringSetImplNode[] children = new StringSetImplNode[ALPHABET_SIZE * 2];
    private int size = 0;

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal() {
        isTerminal = true;
    }

    public void resetTerminal() {
        isTerminal = false;
    }

    public boolean checkChild(char symb) {
        int index = charToInt(symb);
        return children[index] != null;
    }

    public StringSetImplNode getNode(char symb) {
        int index = charToInt(symb);
        return children[index];
    }

    public StringSetImplNode newChild(char symb) {
        int index = charToInt(symb);
        children[index] = new StringSetImplNode();
        return children[index];
    }

    public int getSize() {
        return size;
    }

    public void changeSize(int a) {
        size += a;
    }

    public void deleteChild(char symb) {
        int index = charToInt(symb);
        children[index] = null;
    }


}

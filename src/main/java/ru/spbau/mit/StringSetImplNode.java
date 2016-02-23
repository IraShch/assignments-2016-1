package ru.spbau.mit;


import static ru.spbau.mit.CharToIntConverter.ALPHABET_SIZE;
import static ru.spbau.mit.CharToIntConverter.charToInt;


public class StringSetImplNode {
    private final char symb;
    private boolean isTerminal = false;
    private StringSetImplNode[] children;
    private int size = 0;
    private StringSetImplNode parent = null;

    public StringSetImplNode(char symb) {
        this.symb = symb;
        this.children = new StringSetImplNode[ALPHABET_SIZE * 2];
    }

    public StringSetImplNode(char symb, StringSetImplNode parent) {
        this.symb = symb;
        this.children = new StringSetImplNode[ALPHABET_SIZE * 2];
        this.parent = parent;
    }

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
        children[index] = new StringSetImplNode(symb, this);
        return children[index];
    }

    public int getSize() {
        return size;
    }

    public void deleteChild(char symb) {
        int index = charToInt(symb);
        children[index] = null;
    }

    public void goToRoot(int a) {
        size += a;
        if (size == 0 && symb != ' ') {
            parent.deleteChild(symb);
        }
        if (parent != null) {
            parent.goToRoot(a);
        }
    }

}

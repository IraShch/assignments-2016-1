package ru.spbau.mit;


import static ru.spbau.mit.CharToIntConverter.charToInt;


class StringSetImplNode {

    private static final int ALPHABET_SIZE = 26;

    private final char symbolOnIncomingEdge;
    private boolean isTerminal = false;
    private StringSetImplNode[] children = new StringSetImplNode[ALPHABET_SIZE * 2];
    private int size = 0;
    private StringSetImplNode parent = null;

    StringSetImplNode(char symbolOnIncomingEdge) {
        this.symbolOnIncomingEdge = symbolOnIncomingEdge;
    }

    StringSetImplNode(char symbolOnIncomingEdge, StringSetImplNode parent) {
        this.symbolOnIncomingEdge = symbolOnIncomingEdge;
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
        if (size == 0 && symbolOnIncomingEdge != ' ') {
            parent.deleteChild(symbolOnIncomingEdge);
        }
        if (parent != null) {
            parent.goToRoot(a);
        }
    }

}

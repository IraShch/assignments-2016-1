package ru.spbau.mit;


import java.io.InputStream;
import java.io.OutputStream;

public class StringSetImpl implements StringSet, StreamSerializable {

    private final StringSetImplNode root = new StringSetImplNode();

    @Override
    public boolean add(String element) {
        StringSetImplNode currentNode = root;
        char currentSymbol;
        boolean addedNew = false;

        for (int i = 0; i < element.length(); i++) {
            currentSymbol = element.charAt(i);
            if (currentNode.checkChild(currentSymbol)) {
                currentNode = currentNode.getNode(currentSymbol);
            } else {
                currentNode = currentNode.newChild(currentSymbol);
                addedNew = true;
            }
        }

        if (!addedNew && currentNode.isTerminal()) {
            return false;
        }

        currentNode.setTerminal();
        correctSize(element);
        return true;
    }

    private void correctSize(String element) {
        root.changeSize(1);
        StringSetImplNode currentNode = root;

        for (int i = 0; i < element.length(); i++) {
            currentNode = currentNode.getNode(element.charAt(i));
            currentNode.changeSize(1);
        }
    }

    private StringSetImplNode findLastSymbol(String element) {
        StringSetImplNode currentNode = root;

        for (int i = 0; i < element.length(); i++) {
            char currentSymbol = element.charAt(i);
            if (currentNode.checkChild(currentSymbol)) {
                currentNode = currentNode.getNode(currentSymbol);
            } else {
                return null;
            }
        }

        return currentNode;
    }

    private StringSetImplNode findTerminalVertex(String element) {
        StringSetImplNode lastNode = findLastSymbol(element);

        if (lastNode == null) {
            return null;
        }

        if (lastNode.isTerminal()) {
            return lastNode;
        }

        return null;
    }

    @Override
    public boolean contains(String element) {
        return !(findTerminalVertex(element) == null);
    }


    @Override
    public boolean remove(String element) {
        StringSetImplNode terminalNode = findTerminalVertex(element);

        if (terminalNode == null) {
            return false;
        }

        delete(root, element, 0);
        return true;
    }


    private boolean delete(StringSetImplNode currentNode, String element, int strIndex) {
        boolean needToDelete = false;

        if (strIndex == element.length()) {
            currentNode.resetTerminal();
        } else {
            needToDelete = delete(currentNode.getNode(element.charAt(strIndex)), element, strIndex + 1);
        }

        if (needToDelete) {
            currentNode.deleteChild(element.charAt(strIndex));

        }

        currentNode.changeSize(-1);
        if (currentNode.getSize() == 0 && !currentNode.isTerminal()) {
            return true;
        }

        return false;

    }


    @Override
    public int size() {
        return root.getSize();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        StringSetImplNode terminalNode = findLastSymbol(prefix);

        if (terminalNode == null) {
            return 0;
        }

        return terminalNode.getSize();
    }

    @Override
    public void serialize(OutputStream out) {
        root.serialize(out);
    }

    @Override
    public void deserialize(InputStream in) {
        root.deserialize(in);
    }
}

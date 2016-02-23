package ru.spbau.mit;


public class StringSetImpl implements StringSet {
    private final StringSetImplNode root = new StringSetImplNode(' ');

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
        currentNode.goToRoot(1);
        return true;
    }

    private StringSetImplNode findLastSymbol(String element) {
        StringSetImplNode currentNode = root;
        char currentSymbol;
        for (int i = 0; i < element.length(); i++) {
            currentSymbol = element.charAt(i);
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
        if (terminalNode != null) {
            terminalNode.resetTerminal();
            terminalNode.goToRoot(-1);
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
}

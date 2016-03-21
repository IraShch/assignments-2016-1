package ru.spbau.mit;


import java.io.*;

import static ru.spbau.mit.CharToIntConverter.charToInt;

class StringSetImplNode implements StreamSerializable {

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

    @Override
    public void serialize(OutputStream out) {
        try {
            doSerialize(out);

        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    private void doSerialize(OutputStream out) throws IOException {
        DataOutputStream dataStream = new DataOutputStream(out);
        dataStream.writeBoolean(isTerminal);
        for (int i = 0; i < ALPHABET_SIZE * 2; i++) {
            boolean childFlag = (children[i] != null);
            dataStream.writeBoolean(childFlag);
            if (childFlag) {
                children[i].serialize(out);
            }
        }
    }

    @Override
    public void deserialize(InputStream in) {
        try {
            doDeserialize(in);
        } catch (IOException e) {
            throw new SerializationException();
        }

    }

    private void doDeserialize(InputStream in) throws IOException {
        DataInputStream dataStream = new DataInputStream(in);
        isTerminal = dataStream.readBoolean();
        size = 0;
        if (isTerminal) {
            size++;
        }
        for (int i = 0; i < ALPHABET_SIZE * 2; i++) {
            boolean childFlag = dataStream.readBoolean();
            if (childFlag) {
                children[i] = new StringSetImplNode();
                children[i].deserialize(in);
                size += children[i].size;
            } else {
                children[i] = null;
            }
        }
    }


}

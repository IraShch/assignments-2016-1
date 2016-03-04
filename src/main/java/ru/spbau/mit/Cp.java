package ru.spbau.mit;

import java.io.*;

public class Cp {
    public final static int MAX_SIZE = 1024;

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Provide two arguments!");
        }

        try {
            FileInputStream in = new FileInputStream(args[0]);
            FileOutputStream out = new FileOutputStream(args[1]);

            byte buffer[] = new byte[MAX_SIZE];
            int bytesN;
            while ((bytesN = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesN);
            }
        } catch (IOException e) {
            throw new RuntimeException("File problem");
        }
    }
}

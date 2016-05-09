package ru.spbau.mit;

import java.util.LinkedList;
import java.util.List;

public class BlockingQueue<E> {
    private final List<E> queue = new LinkedList<>();

    public synchronized void put(E item) {
        queue.add(item);
        this.notifyAll();
    }

    public synchronized E take() throws InterruptedException {
        while (queue.size() == 0) {
            this.wait();
        }

        E result = queue.remove(0);
        this.notifyAll();
        return result;
    }

    public int size() {
        return queue.size();
    }
}
